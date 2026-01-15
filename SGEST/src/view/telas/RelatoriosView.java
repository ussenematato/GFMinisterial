package view.telas;

import controller.TransacaoController;
import controller.ContaController;
import util.CurrencyUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RelatoriosView extends JDialog {
    private Integer usuarioId;
    private TransacaoController transacaoController;
    private ContaController contaController;
    
    // Componentes de filtro
    private JComboBox<String> cmbPeriodo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    // Componentes dos painéis
    private JTextArea txtDetalhamentoCompleto;
    private JPanel panelResumoGeral;
    private JPanel panelDespesasCategoria;
    private JPanel panelEvolucaoMensal;
    private JTabbedPane tabbedPane;
    public RelatoriosView(Integer usuarioId) {
        this.usuarioId = usuarioId;
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaController = new ContaController(usuarioId);
        this.dataInicio = LocalDate.now().withDayOfMonth(1);
        this.dataFim = LocalDate.now();
        
        initComponents();
        carregarRelatorios();
    }
    
    private void initComponents() {
        setTitle("Relatórios - Gestão Financeira Ministerial");
        setLayout(new BorderLayout(10, 10));
        setModal(true);
        
        // Painel de filtros
        JPanel panelFiltros = criarPainelFiltros();
        add(panelFiltros, BorderLayout.NORTH);
        
        tabbedPane = new JTabbedPane();
        
        // Aba 1: Detalhamento Completo (Receitas, Despesas, Saldos)
        tabbedPane.addTab("Detalhamento Completo", criarPainelDetalhamentoCompleto());
        
        // Aba 2: Resumo Geral
        panelResumoGeral = criarPainelResumoGeral();
        tabbedPane.addTab("Resumo Geral", panelResumoGeral);
        
        // Aba 3: Despesas por Categoria
        panelDespesasCategoria = criarPainelDespesasCategoria();
        tabbedPane.addTab("Despesas por Categoria", panelDespesasCategoria);
        
        // Aba 4: Evolução Mensal
        panelEvolucaoMensal = criarPainelEvolucaoMensal();
        tabbedPane.addTab("Evolução Mensal", panelEvolucaoMensal);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExportar = new JButton("Exportar Excel");
        JButton btnImprimir = new JButton("Imprimir");
        JButton btnFechar = new JButton("Fechar");
        
        btnExportar.setBackground(new java.awt.Color(34, 139, 34));
        btnExportar.setForeground(java.awt.Color.WHITE);
        btnExportar.addActionListener(e -> exportarParaExcel());
        
        btnImprimir.setBackground(new java.awt.Color(70, 130, 180));
        btnImprimir.setForeground(java.awt.Color.WHITE);
        btnImprimir.addActionListener(e -> imprimir());
        
        btnFechar.setBackground(new java.awt.Color(169, 169, 169));
        btnFechar.setForeground(java.awt.Color.WHITE);
        btnFechar.addActionListener(e -> dispose());
        
        panelBotoes.add(btnExportar);
        panelBotoes.add(btnImprimir);
        panelBotoes.add(btnFechar);
        add(panelBotoes, BorderLayout.SOUTH);
        
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    
    private JPanel criarPainelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        panel.add(new JLabel("Período:"));
        
        cmbPeriodo = new JComboBox<>(new String[]{
            "Hoje",
            "Semana",
            "Mês Atual",
            "Trimestre",
            "Semestre",
            "Ano"
        });
        
        cmbPeriodo.addActionListener(e -> {
            atualizarPeriodo();
            carregarRelatorios();
        });
        
        panel.add(cmbPeriodo);
        
        return panel;
    }
    
    private void atualizarPeriodo() {
        LocalDate hoje = LocalDate.now();
        String periodo = (String) cmbPeriodo.getSelectedItem();
        
        switch (periodo) {
            case "Hoje":
                dataInicio = hoje;
                dataFim = hoje;
                break;
            case "Semana":
                dataInicio = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
                dataFim = hoje;
                break;
            case "Mês Atual":
                dataInicio = hoje.withDayOfMonth(1);
                dataFim = hoje.withDayOfMonth(hoje.lengthOfMonth());
                break;
            case "Trimestre":
                int trimestre = (hoje.getMonthValue() - 1) / 3;
                dataInicio = hoje.withMonth(trimestre * 3 + 1).withDayOfMonth(1);
                dataFim = hoje.withMonth((trimestre + 1) * 3).withDayOfMonth(
                    hoje.withMonth((trimestre + 1) * 3).lengthOfMonth()
                );
                break;
            case "Semestre":
                if (hoje.getMonthValue() <= 6) {
                    dataInicio = hoje.withMonth(1).withDayOfMonth(1);
                    dataFim = hoje.withMonth(6).withDayOfMonth(30);
                } else {
                    dataInicio = hoje.withMonth(7).withDayOfMonth(1);
                    dataFim = hoje.withMonth(12).withDayOfMonth(31);
                }
                break;
            case "Ano":
                dataInicio = hoje.withDayOfYear(1);
                dataFim = hoje.withDayOfYear(hoje.lengthOfYear());
                break;
        }
    }
    
    private JPanel criarPainelDetalhamentoCompleto() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtDetalhamentoCompleto = new JTextArea();
        txtDetalhamentoCompleto.setEditable(false);
        txtDetalhamentoCompleto.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 11));
        
        JScrollPane scrollPane = new JScrollPane(txtDetalhamentoCompleto);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Carregar dados
        atualizarDetalhamentoCompleto();
        
        return panel;
    }
    
    private void atualizarDetalhamentoCompleto() {
        StringBuilder sb = new StringBuilder();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        sb.append("╔════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                    RELATÓRIO FINANCEIRO MINISTERIAL                         ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════════════════╝\n");
        sb.append("\nPeríodo: ").append(dataInicio.format(formatter)).append(" até ").append(dataFim.format(formatter)).append("\n");
        sb.append("\n");
        
        // RECEITAS
        sb.append("┌────────────────────────────────────────────────────────────────────────────┐\n");
        sb.append("│ RECEITAS                                                                   │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        
        List<Object[]> receitas = transacaoController.obterReceitasPorCategoria(dataInicio, dataFim);
        BigDecimal totalReceitas = BigDecimal.ZERO;
        
        if (receitas.isEmpty()) {
            sb.append("│ Sem receitas registradas neste período                                    │\n");
        } else {
            for (Object[] linha : receitas) {
                String categoria = (String) linha[0];
                Double valor = (Double) linha[1];
                BigDecimal bdValor = BigDecimal.valueOf(valor);
                totalReceitas = totalReceitas.add(bdValor);
                
                sb.append(String.format("│ %-45s %s%-15s│\n", 
                    categoria, 
                    " ".repeat(20),
                    CurrencyUtils.formatCurrency(bdValor)));
            }
        }
        
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ TOTAL RECEITAS: %s%-40s│\n", 
            " ".repeat(20),
            CurrencyUtils.formatCurrency(totalReceitas)));
        sb.append("└────────────────────────────────────────────────────────────────────────────┘\n\n");
        
        // DESPESAS
        sb.append("┌────────────────────────────────────────────────────────────────────────────┐\n");
        sb.append("│ DESPESAS                                                                   │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        
        List<Object[]> despesas = transacaoController.obterDespesasPorCategoria(dataInicio, dataFim);
        BigDecimal totalDespesas = BigDecimal.ZERO;
        
        if (despesas.isEmpty()) {
            sb.append("│ Sem despesas registradas neste período                                   │\n");
        } else {
            for (Object[] linha : despesas) {
                String categoria = (String) linha[0];
                Double valor = (Double) linha[1];
                BigDecimal bdValor = BigDecimal.valueOf(valor);
                totalDespesas = totalDespesas.add(bdValor);
                
                sb.append(String.format("│ %-45s %s%-15s│\n", 
                    categoria,
                    " ".repeat(20),
                    CurrencyUtils.formatCurrency(bdValor)));
            }
        }
        
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ TOTAL DESPESAS: %s%-40s│\n", 
            " ".repeat(20),
            CurrencyUtils.formatCurrency(totalDespesas)));
        sb.append("└────────────────────────────────────────────────────────────────────────────┘\n\n");
        
        // SALDO DAS CONTAS
        sb.append("┌────────────────────────────────────────────────────────────────────────────┐\n");
        sb.append("│ SALDO DAS CONTAS                                                           │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        BigDecimal saldoPeriodo = totalReceitas.subtract(totalDespesas);
        
        sb.append(String.format("│ Saldo do Período:  %s%-40s│\n", 
            " ".repeat(20),
            CurrencyUtils.formatCurrency(saldoPeriodo)));
        sb.append(String.format("│ Saldo Total (Contas): %s%-37s│\n", 
            " ".repeat(15),
            CurrencyUtils.formatCurrency(saldoTotal)));
        
        sb.append("└────────────────────────────────────────────────────────────────────────────┘\n");
        
        txtDetalhamentoCompleto.setText(sb.toString());
        txtDetalhamentoCompleto.setCaretPosition(0);
    }
    
    private JPanel criarPainelResumoGeral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Obter dados
        BigDecimal receitas = transacaoController.obterTotalReceitas(dataInicio, dataFim);
        BigDecimal despesas = transacaoController.obterTotalDespesas(dataInicio, dataFim);
        BigDecimal saldoMes = receitas.subtract(despesas);
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        
        // Criar dataset para gráfico de pizza
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Receitas", receitas.doubleValue());
        dataset.setValue("Despesas", despesas.doubleValue());
        
        // Criar gráfico
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribuição Financeira",
            dataset,
            true, // legenda
            true, // tooltips
            false // URLs
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        
        // Painel de estatísticas
        JPanel panelStats = new JPanel(new GridLayout(5, 1, 10, 15));
        panelStats.setBorder(BorderFactory.createTitledBorder("Resumo Período"));
        panelStats.setPreferredSize(new Dimension(400, 400));
        
        // Receitas
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblReceitas = new JLabel(CurrencyUtils.formatCurrency(receitas));
        lblReceitas.setForeground(new java.awt.Color(0, 150, 0));
        lblReceitas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        p1.add(new JLabel("Total Receitas:"));
        p1.add(lblReceitas);
        panelStats.add(p1);
        
        // Despesas
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblDespesas = new JLabel(CurrencyUtils.formatCurrency(despesas));
        lblDespesas.setForeground(new java.awt.Color(200, 0, 0));
        lblDespesas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        p2.add(new JLabel("Total Despesas:"));
        p2.add(lblDespesas);
        panelStats.add(p2);
        
        // Saldo do período
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSaldoMes = new JLabel(CurrencyUtils.formatCurrency(saldoMes));
        if (saldoMes.compareTo(BigDecimal.ZERO) >= 0) {
            lblSaldoMes.setForeground(new java.awt.Color(0, 150, 0));
        } else {
            lblSaldoMes.setForeground(new java.awt.Color(200, 0, 0));
        }
        lblSaldoMes.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        p3.add(new JLabel("Saldo do Período:"));
        p3.add(lblSaldoMes);
        panelStats.add(p3);
        
        // Separador
        JPanel sep = new JPanel();
        sep.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
        panelStats.add(sep);
        
        // Saldo total
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSaldoTotal = new JLabel(CurrencyUtils.formatCurrency(saldoTotal));
        lblSaldoTotal.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        if (saldoTotal.compareTo(BigDecimal.ZERO) >= 0) {
            lblSaldoTotal.setForeground(new java.awt.Color(0, 100, 0));
        } else {
            lblSaldoTotal.setForeground(new java.awt.Color(200, 0, 0));
        }
        p4.add(new JLabel("Saldo Total:"));
        p4.add(lblSaldoTotal);
        panelStats.add(p4);
        
        // Layout
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(panelStats, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel criarPainelDespesasCategoria() {
        JPanel panel = new JPanel(new BorderLayout());
        
        List<Object[]> dados = transacaoController.obterDespesasPorCategoria(dataInicio, dataFim);
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Object[] linha : dados) {
            String categoria = (String) linha[0];
            Double valor = (Double) linha[1];
            dataset.setValue(categoria, valor);
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Despesas por Categoria",
            dataset,
            true,
            true,
            false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        
        // Tabela de detalhes
        String[] colunas = {"Categoria", "Valor", "Percentual"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        
        double total = dados.stream().mapToDouble(d -> (Double) d[1]).sum();
        
        for (Object[] linha : dados) {
            String categoria = (String) linha[0];
            Double valor = (Double) linha[1];
            Double percentual = (valor / total) * 100;
            
            Object[] linhaTabela = {
                categoria,
                CurrencyUtils.formatCurrency(BigDecimal.valueOf(valor)),
                String.format("%.1f%%", percentual)
            };
            model.addRow(linhaTabela);
        }
        
        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel criarPainelEvolucaoMensal() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Dataset para gráfico de linha
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Últimos 6 meses
        LocalDate data = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate mesInicio = data.minusMonths(i).withDayOfMonth(1);
            LocalDate mesFim = data.minusMonths(i).withDayOfMonth(
                data.minusMonths(i).lengthOfMonth()
            );
            
            BigDecimal receitas = transacaoController.obterTotalReceitas(mesInicio, mesFim);
            BigDecimal despesas = transacaoController.obterTotalDespesas(mesInicio, mesFim);
            
            String mesLabel = mesInicio.format(DateTimeFormatter.ofPattern("MM/yy"));
            dataset.addValue(receitas, "Receitas", mesLabel);
            dataset.addValue(despesas, "Despesas", mesLabel);
        }
        
        JFreeChart chart = ChartFactory.createLineChart(
            "Evolução Financeira - Últimos 6 Meses",
            "Mês",
            "Valor (MT)",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void imprimir() {
        JOptionPane.showMessageDialog(this, "Funcionalidade de impressão em desenvolvimento", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportarParaExcel() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório em Excel");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
            
            int resultado = fileChooser.showSaveDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                if (!arquivo.getAbsolutePath().endsWith(".xlsx")) {
                    arquivo = new File(arquivo.getAbsolutePath() + ".xlsx");
                }
                
                criarExcel(arquivo);
                JOptionPane.showMessageDialog(this, "Relatório exportado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void criarExcel(File arquivo) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório Financeiro");
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        
        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        
        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("RELATÓRIO FINANCEIRO MINISTERIAL");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));
        
        // Período
        Row periodRow = sheet.createRow(2);
        periodRow.createCell(0).setCellValue("Período:");
        periodRow.createCell(1).setCellValue(dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
            " até " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // RECEITAS
        Row receitasHeader = sheet.createRow(4);
        receitasHeader.createCell(0).setCellValue("RECEITAS");
        receitasHeader.getCell(0).setCellStyle(titleStyle);
        
        Row colHeader1 = sheet.createRow(5);
        colHeader1.createCell(0).setCellValue("Categoria");
        colHeader1.createCell(1).setCellValue("Valor");
        colHeader1.getCell(0).setCellStyle(headerStyle);
        colHeader1.getCell(1).setCellStyle(headerStyle);
        
        List<Object[]> receitas = transacaoController.obterReceitasPorCategoria(dataInicio, dataFim);
        BigDecimal totalReceitas = BigDecimal.ZERO;
        int rowNum = 6;
        
        for (Object[] linha : receitas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) linha[0]);
            Double valor = (Double) linha[1];
            row.createCell(1).setCellValue(valor);
            row.getCell(1).setCellStyle(currencyStyle);
            totalReceitas = totalReceitas.add(BigDecimal.valueOf(valor));
        }
        
        Row totalReceitasRow = sheet.createRow(rowNum++);
        totalReceitasRow.createCell(0).setCellValue("TOTAL RECEITAS");
        totalReceitasRow.createCell(1).setCellValue(totalReceitas.doubleValue());
        totalReceitasRow.getCell(0).setCellStyle(headerStyle);
        totalReceitasRow.getCell(1).setCellStyle(headerStyle);
        
        // DESPESAS
        Row despesasHeader = sheet.createRow(rowNum + 2);
        despesasHeader.createCell(0).setCellValue("DESPESAS");
        despesasHeader.getCell(0).setCellStyle(titleStyle);
        
        Row colHeader2 = sheet.createRow(rowNum + 3);
        colHeader2.createCell(0).setCellValue("Categoria");
        colHeader2.createCell(1).setCellValue("Valor");
        colHeader2.getCell(0).setCellStyle(headerStyle);
        colHeader2.getCell(1).setCellStyle(headerStyle);
        
        List<Object[]> despesas = transacaoController.obterDespesasPorCategoria(dataInicio, dataFim);
        BigDecimal totalDespesas = BigDecimal.ZERO;
        rowNum = rowNum + 4;
        
        for (Object[] linha : despesas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) linha[0]);
            Double valor = (Double) linha[1];
            row.createCell(1).setCellValue(valor);
            row.getCell(1).setCellStyle(currencyStyle);
            totalDespesas = totalDespesas.add(BigDecimal.valueOf(valor));
        }
        
        Row totalDespesasRow = sheet.createRow(rowNum++);
        totalDespesasRow.createCell(0).setCellValue("TOTAL DESPESAS");
        totalDespesasRow.createCell(1).setCellValue(totalDespesas.doubleValue());
        totalDespesasRow.getCell(0).setCellStyle(headerStyle);
        totalDespesasRow.getCell(1).setCellStyle(headerStyle);
        
        // SALDO
        Row saldoHeader = sheet.createRow(rowNum + 2);
        saldoHeader.createCell(0).setCellValue("SALDO");
        saldoHeader.getCell(0).setCellStyle(titleStyle);
        
        Row saldoPeriodoRow = sheet.createRow(rowNum + 3);
        saldoPeriodoRow.createCell(0).setCellValue("Saldo do Período");
        BigDecimal saldoPeriodo = totalReceitas.subtract(totalDespesas);
        saldoPeriodoRow.createCell(1).setCellValue(saldoPeriodo.doubleValue());
        saldoPeriodoRow.getCell(1).setCellStyle(currencyStyle);
        
        Row saldoTotalRow = sheet.createRow(rowNum + 4);
        saldoTotalRow.createCell(0).setCellValue("Saldo Total");
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        saldoTotalRow.createCell(1).setCellValue(saldoTotal.doubleValue());
        saldoTotalRow.getCell(1).setCellStyle(currencyStyle);
        
        // Ajustar largura das colunas
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 3000);
        
        try (FileOutputStream out = new FileOutputStream(arquivo)) {
            workbook.write(out);
        }
        workbook.close();
    }
    
    private void carregarRelatorios() {
        // Atualizar todos os painéis
        atualizarDetalhamentoCompleto();
        
        // Reconstruir Resumo Geral
        tabbedPane.setComponentAt(1, criarPainelResumoGeral());
        
        // Reconstruir Despesas por Categoria
        tabbedPane.setComponentAt(2, criarPainelDespesasCategoria());
        
        // Reconstruir Evolução Mensal
        tabbedPane.setComponentAt(3, criarPainelEvolucaoMensal());
    }
}
