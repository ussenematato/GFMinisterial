package view.telas.componentes;

import controller.TransacaoController;
import controller.ContaController;
import controller.CategoriaController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.cj.result.Row;
import com.toedter.calendar.JDateChooser;

public class RelatoriosCard extends CardBase {
    
    private TransacaoController transacaoController;
    private ContaController contaController;
    private CategoriaController categoriaController;
    
    // Componentes da UI
    private JTabbedPane tabbedPane;
    private JComboBox<String> cbPeriodo;
    private JComboBox<String> cbTipoRelatorio;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbConta;
    private JDateChooser dateInicio;
    private JDateChooser dateFim;
    private JButton btnFiltrar;
    private JButton btnLimparFiltros;
    private JButton btnExportarPDF;
    private JButton btnExportarExcel;
    
    // Painéis de gráficos
    private ChartPanel chartResumoPanel;
    private ChartPanel chartCategoriaPanel;
    private ChartPanel chartEvolucaoPanel;
    
    // Tabelas
    private JTable tabelaDetalhamento;
    private DefaultTableModel modeloTabela;
    
    // Filtros atuais
    private LocalDate filtroInicio;
    private LocalDate filtroFim;
    private String filtroTipo;
    private String filtroCategoria;
    private String filtroConta;
    
    public RelatoriosCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaController = new ContaController(usuarioId);
        this.categoriaController = new CategoriaController(usuarioId);
        
        initComponents();
        configurarFiltros();
        carregarDados();
    }
    
    @Override
    public void carregarDados() {
        aplicarFiltros();
        atualizarGraficos();
        atualizarTabelaDetalhamento();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel de filtros
        JPanel panelFiltros = criarPainelFiltros();
        
        // Painel de abas
        tabbedPane = new JTabbedPane();
        
        // Criar as abas
        tabbedPane.addTab("Resumo Geral", criarPainelResumoGeral());
        tabbedPane.addTab("Despesas por Categoria", criarPainelDespesasCategoria());
        tabbedPane.addTab("Evolução Mensal", criarPainelEvolucaoMensal());
        tabbedPane.addTab("Detalhamento", criarPainelDetalhamento());
        
        // Painel de botões de exportação
        JPanel panelExportacao = criarPainelExportacao();
        
        // Adicionar componentes ao card
        add(panelFiltros, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(panelExportacao, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelFiltros() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Período
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Período:"), gbc);
        
        String[] periodos = {"Mês Atual", "Mês Anterior", "Últimos 3 Meses", "Últimos 6 Meses", "Ano Atual", "Personalizado"};
        cbPeriodo = new JComboBox<>(periodos);
        cbPeriodo.addActionListener(e -> atualizarPeriodo());
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(cbPeriodo, gbc);
        
        // Data Início
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("De:"), gbc);
        
        dateInicio = new JDateChooser();
        dateInicio.setDateFormatString("dd/MM/yyyy");
        gbc.gridx = 3; gbc.gridy = 0;
        panel.add(dateInicio, gbc);
        
        // Data Fim
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Até:"), gbc);
        
        dateFim = new JDateChooser();
        dateFim.setDateFormatString("dd/MM/yyyy");
        gbc.gridx = 5; gbc.gridy = 0;
        panel.add(dateFim, gbc);
        
        // Tipo de Relatório
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tipo:"), gbc);
        
        String[] tipos = {"Todos", "Receitas", "Despesas", "Transferências"};
        cbTipoRelatorio = new JComboBox<>(tipos);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(cbTipoRelatorio, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Categoria:"), gbc);
        
        cbCategoria = new JComboBox<>();
        cbCategoria.addItem("Todas");
        gbc.gridx = 3; gbc.gridy = 1;
        panel.add(cbCategoria, gbc);
        
        // Conta
        gbc.gridx = 4; gbc.gridy = 1;
        panel.add(new JLabel("Conta:"), gbc);
        
        cbConta = new JComboBox<>();
        cbConta.addItem("Todas");
        gbc.gridx = 5; gbc.gridy = 1;
        panel.add(cbConta, gbc);
        
        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnFiltrar = criarBotao("Aplicar Filtros", new Color(70, 130, 180));
        btnLimparFiltros = criarBotao("Limpar Filtros", new Color(169, 169, 169));
        
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnLimparFiltros.addActionListener(e -> limparFiltros());
        
        panelBotoes.add(btnFiltrar);
        panelBotoes.add(btnLimparFiltros);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 6;
        panel.add(panelBotoes, gbc);
        
        return panel;
    }
    
    private JPanel criarPainelResumoGeral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Painel do gráfico
        JPanel panelGrafico = new JPanel(new BorderLayout());
        panelGrafico.setBorder(BorderFactory.createTitledBorder("Distribuição Financeira"));
        
        // Gráfico será criado dinamicamente
        chartResumoPanel = new ChartPanel(null);
        chartResumoPanel.setPreferredSize(new Dimension(600, 400));
        panelGrafico.add(chartResumoPanel, BorderLayout.CENTER);
        
        // Painel de estatísticas
        JPanel panelStats = criarPainelEstatisticas();
        
        panel.add(panelGrafico, BorderLayout.CENTER);
        panel.add(panelStats, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel criarPainelEstatisticas() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Estatísticas"));
        panel.setPreferredSize(new Dimension(300, 400));
        
        // Os valores serão atualizados dinamicamente
        String[] labels = {"Período:", "Total Receitas:", "Total Despesas:", "Saldo Período:", 
                           "Média Diária:", "Maior Receita:", "Maior Despesa:", "Saldo Total:"};
        
        for (String label : labels) {
            JPanel linha = new JPanel(new BorderLayout());
            linha.add(new JLabel(label), BorderLayout.WEST);
            linha.add(new JLabel("---"), BorderLayout.CENTER);
            panel.add(linha);
        }
        
        return panel;
    }
    
    private JPanel criarPainelDespesasCategoria() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Gráfico
        JPanel panelGrafico = new JPanel(new BorderLayout());
        panelGrafico.setBorder(BorderFactory.createTitledBorder("Distribuição por Categoria"));
        
        chartCategoriaPanel = new ChartPanel(null);
        panelGrafico.add(chartCategoriaPanel, BorderLayout.CENTER);
        
        // Tabela
        JPanel panelTabela = new JPanel(new BorderLayout());
        panelTabela.setBorder(BorderFactory.createTitledBorder("Detalhes por Categoria"));
        
        String[] colunas = {"Categoria", "Valor (R$)", "Percentual", "Qtde Transações"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaDetalhamento = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaDetalhamento);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        panelTabela.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(panelGrafico, BorderLayout.CENTER);
        panel.add(panelTabela, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel criarPainelEvolucaoMensal() {
        JPanel panel = new JPanel(new BorderLayout());
        
        chartEvolucaoPanel = new ChartPanel(null);
        chartEvolucaoPanel.setPreferredSize(new Dimension(800, 500));
        
        panel.add(chartEvolucaoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelDetalhamento() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea txtDetalhes = new JTextArea();
        txtDetalhes.setEditable(false);
        txtDetalhes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(txtDetalhes);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelExportacao() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        btnExportarPDF = criarBotao("Exportar PDF", new Color(220, 53, 69));
        btnExportarExcel = criarBotao("Exportar Excel", new Color(40, 167, 69));
        
        btnExportarPDF.addActionListener(e -> exportarParaPDF());
        btnExportarExcel.addActionListener(e -> exportarParaExcel());
        
        panel.add(btnExportarPDF);
        panel.add(btnExportarExcel);
        
        return panel;
    }
    
    private void configurarFiltros() {
        // Configurar datas padrão (mês atual)
        YearMonth mesAtual = YearMonth.now();
        dateInicio.setDate(java.sql.Date.valueOf(mesAtual.atDay(1)));
        dateFim.setDate(java.sql.Date.valueOf(mesAtual.atEndOfMonth()));
        
        // Carregar categorias
        List<String> categorias = categoriaController.listarNomesCategorias();
        for (String categoria : categorias) {
            cbCategoria.addItem(categoria);
        }
        
        // Carregar contas
        List<String> contas = contaController.listarNomesContas();
        for (String conta : contas) {
            cbConta.addItem(conta);
        }
    }
    
    private void atualizarPeriodo() {
        String periodo = (String) cbPeriodo.getSelectedItem();
        LocalDate hoje = LocalDate.now();
        
        switch (periodo) {
            case "Mês Atual":
                YearMonth mesAtual = YearMonth.now();
                dateInicio.setDate(java.sql.Date.valueOf(mesAtual.atDay(1)));
                dateFim.setDate(java.sql.Date.valueOf(mesAtual.atEndOfMonth()));
                break;
                
            case "Mês Anterior":
                YearMonth mesAnterior = YearMonth.now().minusMonths(1);
                dateInicio.setDate(java.sql.Date.valueOf(mesAnterior.atDay(1)));
                dateFim.setDate(java.sql.Date.valueOf(mesAnterior.atEndOfMonth()));
                break;
                
            case "Últimos 3 Meses":
                dateInicio.setDate(java.sql.Date.valueOf(hoje.minusMonths(3).withDayOfMonth(1)));
                dateFim.setDate(java.sql.Date.valueOf(hoje));
                break;
                
            case "Últimos 6 Meses":
                dateInicio.setDate(java.sql.Date.valueOf(hoje.minusMonths(6).withDayOfMonth(1)));
                dateFim.setDate(java.sql.Date.valueOf(hoje));
                break;
                
            case "Ano Atual":
                dateInicio.setDate(java.sql.Date.valueOf(LocalDate.of(hoje.getYear(), 1, 1)));
                dateFim.setDate(java.sql.Date.valueOf(hoje));
                break;
                
            case "Personalizado":
                // Mantém as datas selecionadas
                break;
        }
    }
    
    private void aplicarFiltros() {
        // Capturar valores dos filtros
        filtroInicio = ((java.sql.Date) dateInicio.getDate()).toLocalDate();
        filtroFim = ((java.sql.Date) dateFim.getDate()).toLocalDate();
        filtroTipo = (String) cbTipoRelatorio.getSelectedItem();
        filtroCategoria = (String) cbCategoria.getSelectedItem();
        filtroConta = (String) cbConta.getSelectedItem();
        
        // Atualizar dados
        atualizarGraficos();
        atualizarTabelaDetalhamento();
    }
    
    private void limparFiltros() {
        cbPeriodo.setSelectedIndex(0); // Mês Atual
        cbTipoRelatorio.setSelectedIndex(0); // Todos
        cbCategoria.setSelectedIndex(0); // Todas
        cbConta.setSelectedIndex(0); // Todas
        
        atualizarPeriodo();
        aplicarFiltros();
    }
    
    private void atualizarGraficos() {
        // Atualizar gráfico de resumo
        atualizarGraficoResumo();
        
        // Atualizar gráfico de categorias
        atualizarGraficoCategorias();
        
        // Atualizar gráfico de evolução
        atualizarGraficoEvolucao();
    }
    
    private void atualizarGraficoResumo() {
        BigDecimal receitas = transacaoController.obterTotalReceitas(filtroInicio, filtroFim);
        BigDecimal despesas = transacaoController.obterTotalDespesas(filtroInicio, filtroFim);
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        if (receitas.compareTo(BigDecimal.ZERO) > 0) {
            dataset.setValue("Receitas", receitas.doubleValue());
        }
        if (despesas.compareTo(BigDecimal.ZERO) > 0) {
            dataset.setValue("Despesas", despesas.doubleValue());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribuição Financeira",
            dataset,
            true, true, false
        );
        
        if (chartResumoPanel != null) {
            chartResumoPanel.setChart(chart);
        }
    }
    
    private void atualizarGraficoCategorias() {
        List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Object[] linha : dados) {
            String categoria = (String) linha[0];
            Double valor = (Double) linha[1];
            if (valor > 0) {
                dataset.setValue(categoria, valor);
            }
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Despesas por Categoria",
            dataset,
            true, true, false
        );
        
        if (chartCategoriaPanel != null) {
            chartCategoriaPanel.setChart(chart);
        }
    }
    
    private void atualizarGraficoEvolucao() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Últimos 6 meses dentro do período filtrado
        LocalDate inicio = filtroInicio;
        LocalDate fim = filtroFim;
        
        // Calcular intervalos mensais
        List<YearMonth> meses = new ArrayList<>();
        YearMonth mesAtual = YearMonth.from(inicio);
        YearMonth mesFim = YearMonth.from(fim);
        
        while (!mesAtual.isAfter(mesFim)) {
            meses.add(mesAtual);
            mesAtual = mesAtual.plusMonths(1);
        }
        
        // Limitar a 12 meses para melhor visualização
        if (meses.size() > 12) {
            meses = meses.subList(meses.size() - 12, meses.size());
        }
        
        for (YearMonth mes : meses) {
            LocalDate mesInicio = mes.atDay(1);
            LocalDate mesFimCalculado = mes.atEndOfMonth();
            
            // Ajustar para o período filtrado
            if (mesInicio.isBefore(inicio)) mesInicio = inicio;
            if (mesFimCalculado.isAfter(fim)) mesFimCalculado = fim;
            
            BigDecimal receitas = transacaoController.obterTotalReceitas(mesInicio, mesFimCalculado);
            BigDecimal despesas = transacaoController.obterTotalDespesas(mesInicio, mesFimCalculado);
            
            dataset.addValue(receitas, "Receitas", mes.format(DateTimeFormatter.ofPattern("MM/yy")));
            dataset.addValue(despesas, "Despesas", mes.format(DateTimeFormatter.ofPattern("MM/yy")));
        }
        
        JFreeChart chart = ChartFactory.createLineChart(
            "Evolução Financeira",
            "Mês",
            "Valor (R$)",
            dataset
        );
        
        if (chartEvolucaoPanel != null) {
            chartEvolucaoPanel.setChart(chart);
        }
    }
    
    private void atualizarTabelaDetalhamento() {
        modeloTabela.setRowCount(0);
        
        List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
        double total = dados.stream().mapToDouble(d -> (Double) d[1]).sum();
        
        for (Object[] linha : dados) {
            String categoria = (String) linha[0];
            Double valor = (Double) linha[1];
            Long quantidade = (Long) linha[2];
            Double percentual = total > 0 ? (valor / total) * 100 : 0;
            
            Object[] linhaTabela = {
                categoria,
                String.format("R$ %,.2f", valor),
                String.format("%.1f%%", percentual),
                quantidade
            };
            modeloTabela.addRow(linhaTabela);
        }
    }
    
    private void exportarParaPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório PDF");
        fileChooser.setSelectedFile(new File("relatorio_financeiro_" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                document.open();
                
                // Título
                var titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                Paragraph title = new Paragraph("Relatório Financeiro", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                
                // Período
                var normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                Paragraph periodo = new Paragraph(
                    "Período: " + filtroInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                    " a " + filtroFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                    normalFont
                );
                document.add(periodo);
                
                document.add(new Paragraph(" "));
                
                // Tabela de resumo
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                
                // Cabeçalho
                table.addCell(new PdfPCell(new Phrase("Categoria", normalFont)));
                table.addCell(new PdfPCell(new Phrase("Valor (R$)", normalFont)));
                table.addCell(new PdfPCell(new Phrase("Percentual", normalFont)));
                table.addCell(new PdfPCell(new Phrase("Qtde Transações", normalFont)));
                
                // Dados
                List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
                double total = dados.stream().mapToDouble(d -> (Double) d[1]).sum();
                
                for (Object[] linha : dados) {
                    String categoria = (String) linha[0];
                    Double valor = (Double) linha[1];
                    Long quantidade = (Long) linha[2];
                    Double percentual = total > 0 ? (valor / total) * 100 : 0;
                    
                    table.addCell(categoria);
                    table.addCell(String.format("R$ %,.2f", valor));
                    table.addCell(String.format("%.1f%%", percentual));
                    table.addCell(quantidade.toString());
                }
                
                document.add(table);
                document.close();
                
                mostrarMensagemSucesso("Relatório PDF exportado com sucesso!");
                
            } catch (Exception e) {
                mostrarMensagemErro("Erro ao exportar PDF: " + e.getMessage());
            }
        }
    }
    
    private void exportarParaExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório Excel");
        fileChooser.setSelectedFile(new File("relatorio_financeiro_" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Relatório Financeiro");
                
                // Cabeçalho
                Row headerRow = sheet.createRow(0);
                String[] headers = {"Categoria", "Valor (R$)", "Percentual", "Qtde Transações", "Período"};
                
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // Dados
                List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
                double total = dados.stream().mapToDouble(d -> (Double) d[1]).sum();
                
                int rowNum = 1;
                for (Object[] linha : dados) {
                    Row row = sheet.createRow(rowNum++);
                    
                    String categoria = (String) linha[0];
                    Double valor = (Double) linha[1];
                    Long quantidade = (Long) linha[2];
                    Double percentual = total > 0 ? (valor / total) * 100 : 0;
                    
                    row.createCell(0).setCellValue(categoria);
                    row.createCell(1).setCellValue(valor);
                    row.createCell(2).setCellValue(percentual);
                    row.createCell(3).setCellValue(quantidade);
                    row.createCell(4).setCellValue(
                        filtroInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                        " a " + 
                        filtroFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    );
                }
                
                // Auto-size columns
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // Salvar arquivo
                try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                    workbook.write(fileOut);
                }
                
                mostrarMensagemSucesso("Relatório Excel exportado com sucesso!");
                
            } catch (Exception e) {
                mostrarMensagemErro("Erro ao exportar Excel: " + e.getMessage());
            }
        }
    }
}