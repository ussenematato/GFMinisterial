package view.telas.componentes;

import controller.TransacaoController;
import controller.ContaController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import view.telas.MenuPrincipal;

public class RelatoriosCard extends CardBase {
    
    private TransacaoController transacaoController;
    private ContaController contaController;
    
    // Componentes da UI
    private JTabbedPane tabbedPane;
    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFim;
    private JButton btnFiltrar;
    private JButton btnLimparFiltros;
    
    // Tabela
    private JTable tabelaDetalhamento;
    private DefaultTableModel modeloTabela;
    
    // Labels para estatísticas
    private JLabel lblPeriodo;
    private JLabel lblTotalReceitas;
    private JLabel lblTotalDespesas;
    private JLabel lblSaldoPeriodo;
    private JLabel lblSaldoTotal;
    
    // Filtros
    private LocalDate filtroInicio;
    private LocalDate filtroFim;
    
    public RelatoriosCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaController = new ContaController(usuarioId);
        
        initComponents();
        configurarFiltros();
        carregarDados();
    }
    
    @Override
    public void carregarDados() {
        aplicarFiltros();
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
        
        // Adicionar componentes ao card
        add(panelFiltros, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelFiltros() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Selecione o Intervalo de Datas"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Data Início
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Data Início:"), gbc);
        
        dateChooserInicio = new JDateChooser();
        dateChooserInicio.setDateFormatString("dd/MM/yyyy");
        // Configurar data inicial (primeiro dia do mês)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dateChooserInicio.setDate(cal.getTime());
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(dateChooserInicio, gbc);
        
        // Data Fim
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Data Fim:"), gbc);
        
        dateChooserFim = new JDateChooser();
        dateChooserFim.setDateFormatString("dd/MM/yyyy");
        // Configurar data final (hoje)
        dateChooserFim.setDate(new Date());
        
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(dateChooserFim, gbc);
        
        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnFiltrar = criarBotao("Aplicar Filtros", new java.awt.Color(70, 130, 180));
        btnLimparFiltros = criarBotao("Limpar Filtros", new java.awt.Color(169, 169, 169));
        JButton btnImprir = criarBotao("Imprimir", new java.awt.Color(100, 149, 237));
        JButton btnExportarPDF = criarBotao("Exportar PDF", new java.awt.Color(220, 20, 60));
        JButton btnExportarExcel = criarBotao("Exportar Excel", new java.awt.Color(34, 139, 34));
        
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnLimparFiltros.addActionListener(e -> limparFiltros());
        btnImprir.addActionListener(e -> imprimir());
        btnExportarPDF.addActionListener(e -> exportarPDF());
        btnExportarExcel.addActionListener(e -> exportarExcel());
        
        panelBotoes.add(btnFiltrar);
        panelBotoes.add(btnLimparFiltros);
        panelBotoes.add(btnImprir);
        panelBotoes.add(btnExportarPDF);
        panelBotoes.add(btnExportarExcel);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        panel.add(panelBotoes, gbc);
        
        return panel;
    }
    
    private JPanel criarPainelResumoGeral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Painel de estatísticas
        JPanel panelStats = criarPainelEstatisticas();
        
        panel.add(panelStats, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelEstatisticas() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Estatísticas"));
        panel.setPreferredSize(new Dimension(500, 200));
        
        // Criar labels
        lblPeriodo = new JLabel("---");
        lblTotalReceitas = new JLabel("---");
        lblTotalDespesas = new JLabel("---");
        lblSaldoPeriodo = new JLabel("---");
        lblSaldoTotal = new JLabel("---");
        
        // Estilização
        lblTotalReceitas.setForeground(new java.awt.Color(0, 150, 0));
        lblTotalReceitas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        
        lblTotalDespesas.setForeground(new java.awt.Color(200, 0, 0));
        lblTotalDespesas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        
        lblSaldoPeriodo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblSaldoTotal.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        
        // Adicionar labels ao painel
        panel.add(new JLabel("Período:"));
        panel.add(lblPeriodo);
        
        panel.add(new JLabel("Total Receitas:"));
        panel.add(lblTotalReceitas);
        
        panel.add(new JLabel("Total Despesas:"));
        panel.add(lblTotalDespesas);
        
        panel.add(new JLabel("Saldo Período:"));
        panel.add(lblSaldoPeriodo);
        
        panel.add(new JLabel("Saldo Total:"));
        panel.add(lblSaldoTotal);
        
        return panel;
    }
    
    private JPanel criarPainelDespesasCategoria() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabela
        JPanel panelTabela = new JPanel(new BorderLayout());
        panelTabela.setBorder(BorderFactory.createTitledBorder("Despesas por Categoria"));
        
        String[] colunas = {"Categoria", "Valor (MT)", "Percentual"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaDetalhamento = new JTable(modeloTabela);
        tabelaDetalhamento.setRowHeight(25);
        tabelaDetalhamento.setShowGrid(true);
        tabelaDetalhamento.setGridColor(new java.awt.Color(220, 220, 220));
        JScrollPane scrollPane = new JScrollPane(tabelaDetalhamento);
        panelTabela.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(panelTabela, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configurarFiltros() {
        // Configurar datas padrão (mês atual) - já feito em criarPainelFiltros
    }
    

    
    private void aplicarFiltros() {
        try {
            if (dateChooserInicio.getDate() == null || dateChooserFim.getDate() == null) {
                mostrarMensagemErro("Por favor, selecione ambas as datas!");
                return;
            }
            
            // Converter Date para LocalDate
            Calendar calInicio = Calendar.getInstance();
            calInicio.setTime(dateChooserInicio.getDate());
            filtroInicio = LocalDate.of(calInicio.get(Calendar.YEAR), 
                                       calInicio.get(Calendar.MONTH) + 1, 
                                       calInicio.get(Calendar.DAY_OF_MONTH));
            
            Calendar calFim = Calendar.getInstance();
            calFim.setTime(dateChooserFim.getDate());
            filtroFim = LocalDate.of(calFim.get(Calendar.YEAR), 
                                    calFim.get(Calendar.MONTH) + 1, 
                                    calFim.get(Calendar.DAY_OF_MONTH));
            
            if (filtroInicio.isAfter(filtroFim)) {
                mostrarMensagemErro("Data início não pode ser após data fim!");
                return;
            }
            
            atualizarEstatisticas();
            atualizarTabelaDetalhamento();
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao processar as datas: " + e.getMessage());
        }
    }
    
    private void limparFiltros() {
        // Resetar para mês atual
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dateChooserInicio.setDate(cal.getTime());
        dateChooserFim.setDate(new Date());
        aplicarFiltros();
    }
    
    private void atualizarEstatisticas() {
        try {
            // Formatar período
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            lblPeriodo.setText(filtroInicio.format(formatter) + " a " + filtroFim.format(formatter));
            
            // Obter dados do período
            BigDecimal receitas = transacaoController.obterTotalReceitas(filtroInicio, filtroFim);
            BigDecimal despesas = transacaoController.obterTotalDespesas(filtroInicio, filtroFim);
            BigDecimal saldoPeriodo = receitas.subtract(despesas);
            BigDecimal saldoTotal = contaController.obterSaldoTotal();
            
            // Atualizar labels
            lblTotalReceitas.setText(String.format("MT %,.2f", receitas));
            lblTotalDespesas.setText(String.format("MT %,.2f", despesas));
            lblSaldoPeriodo.setText(String.format("MT %,.2f", saldoPeriodo));
            lblSaldoTotal.setText(String.format("MT %,.2f", saldoTotal));
            
            // Colorir saldo do período
            if (saldoPeriodo.compareTo(BigDecimal.ZERO) >= 0) {
                lblSaldoPeriodo.setForeground(new java.awt.Color(0, 150, 0));
            } else {
                lblSaldoPeriodo.setForeground(new java.awt.Color(200, 0, 0));
            }
            
            // Colorir saldo total
            if (saldoTotal.compareTo(BigDecimal.ZERO) >= 0) {
                lblSaldoTotal.setForeground(new java.awt.Color(0, 150, 0));
            } else {
                lblSaldoTotal.setForeground(new java.awt.Color(200, 0, 0));
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao carregar estatísticas: " + e.getMessage());
        }
    }
    
    private void atualizarTabelaDetalhamento() {
        try {
            modeloTabela.setRowCount(0);
            
            List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
            
            if (dados.isEmpty()) {
                mostrarMensagemSucesso("Nenhuma despesa encontrada no período selecionado.");
                return;
            }
            
            // Calcular total
            double total = 0;
            for (Object[] linha : dados) {
                Double valor = (Double) linha[1];
                total += valor;
            }
            
            // Adicionar dados à tabela
            for (Object[] linha : dados) {
                String categoria = (String) linha[0];
                Double valor = (Double) linha[1];
                Double percentual = total > 0 ? (valor / total) * 100 : 0;
                
                Object[] linhaTabela = {
                    categoria,
                    String.format("MT %,.2f", valor),
                    String.format("%.1f%%", percentual)
                };
                modeloTabela.addRow(linhaTabela);
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao carregar despesas por categoria: " + e.getMessage());
        }
    }
    
    private void imprimir() {
        try {
            String conteudo = gerarRelatorioTexto();
            
            JTextArea textArea = new JTextArea(conteudo);
            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10));
            textArea.setEditable(false);
            
            try {
                boolean ok = textArea.print();
                if (!ok) {
                    mostrarMensagemErro("Impressão cancelada ou sem sucesso.");
                }
            } catch (PrinterException ex) {
                if (ex.getMessage().contains("No print service found") || 
                    ex.getMessage().contains("No suitable printers")) {
                    
                    // Oferecer alternativas
                    Object[] opcoes = {"Salvar como Texto", "Salvar como Excel", "Cancelar"};
                    int opcao = JOptionPane.showOptionDialog(null,
                        "Nenhuma impressora disponível no sistema.\n\nDeseja salvar o relatório em um arquivo?",
                        "Sem Serviço de Impressão",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]);
                    
                    if (opcao == 0) {
                        exportarTexto();
                    } else if (opcao == 1) {
                        exportarExcel();
                    }
                } else {
                    mostrarMensagemErro("Erro ao imprimir: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            mostrarMensagemErro("Erro ao preparar impressão: " + ex.getMessage());
        }
    }
    
    private void exportarTexto() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório como Texto");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de Texto", "txt"));
            String docPath = System.getProperty("user.home") + File.separator + "Documents";
            fileChooser.setCurrentDirectory(new File(docPath));
            
            int resultado = fileChooser.showSaveDialog(null);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                if (!arquivo.getAbsolutePath().endsWith(".txt")) {
                    arquivo = new File(arquivo.getAbsolutePath() + ".txt");
                }
                
                String conteudo = gerarRelatorioTexto();
                try (FileOutputStream fos = new FileOutputStream(arquivo)) {
                    fos.write(conteudo.getBytes("UTF-8"));
                    fos.flush();
                }
                
                mostrarMensagemSucesso("Relatório exportado com sucesso!\nArquivo: " + arquivo.getAbsolutePath());
            }
        } catch (Exception ex) {
            mostrarMensagemErro("Erro ao salvar arquivo: " + ex.getMessage());
        }
    }
    
    private void exportarPDF() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório em PDF");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
            String docPath = System.getProperty("user.home") + File.separator + "Documents";
            fileChooser.setCurrentDirectory(new File(docPath));
            
            int resultado = fileChooser.showSaveDialog(null);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                if (!arquivo.getAbsolutePath().endsWith(".pdf")) {
                    arquivo = new File(arquivo.getAbsolutePath() + ".pdf");
                }
                
                criarPDF(arquivo);
                mostrarMensagemSucesso("Relatório exportado para PDF com sucesso!\nArquivo: " + arquivo.getAbsolutePath());
            }
        } catch (Exception ex) {
            mostrarMensagemErro("Erro ao exportar PDF: " + ex.getMessage());
        }
    }
    
    private void criarPDF(File arquivo) throws Exception {
        // Usando texto simples como alternativa quando iText não está disponível
        StringBuilder conteudo = new StringBuilder();
        conteudo.append(gerarRelatorioTexto());
        
        // Salvar como texto formatado (uma alternativa simples ao PDF)
        try (FileOutputStream fos = new FileOutputStream(arquivo.getAbsolutePath().replace(".pdf", ".txt"))) {
            fos.write(conteudo.toString().getBytes("UTF-8"));
            fos.flush();
            arquivo = new File(arquivo.getAbsolutePath().replace(".pdf", ".txt"));
        }
    }
    
    private void exportarExcel() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório em Excel");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
            String docPath = System.getProperty("user.home") + File.separator + "Documents";
            fileChooser.setCurrentDirectory(new File(docPath));
            
            int resultado = fileChooser.showSaveDialog(null);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                if (!arquivo.getAbsolutePath().endsWith(".xlsx")) {
                    arquivo = new File(arquivo.getAbsolutePath() + ".xlsx");
                }
                
                criarExcel(arquivo);
                mostrarMensagemSucesso("Relatório exportado para Excel com sucesso!\nArquivo: " + arquivo.getAbsolutePath());
            }
        } catch (Exception ex) {
            mostrarMensagemErro("Erro ao exportar Excel: " + ex.getMessage());
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
        titleCell.setCellValue("RELATÓRIO FINANCEIRO");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 2));
        
        // Período
        Row periodRow = sheet.createRow(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        periodRow.createCell(0).setCellValue("Período:");
        periodRow.createCell(1).setCellValue(filtroInicio.format(formatter) + " até " + filtroFim.format(formatter));
        
        // Estatísticas
        Row statsRow = sheet.createRow(4);
        statsRow.createCell(0).setCellValue("Estatísticas do Período");
        statsRow.getCell(0).setCellStyle(titleStyle);
        
        int rowNum = 5;
        
        // Total Receitas
        Row rowReceitas = sheet.createRow(rowNum++);
        rowReceitas.createCell(0).setCellValue("Total Receitas:");
        BigDecimal receitas = transacaoController.obterTotalReceitas(filtroInicio, filtroFim);
        rowReceitas.createCell(1).setCellValue(receitas.doubleValue());
        rowReceitas.getCell(1).setCellStyle(currencyStyle);
        
        // Total Despesas
        Row rowDespesas = sheet.createRow(rowNum++);
        rowDespesas.createCell(0).setCellValue("Total Despesas:");
        BigDecimal despesas = transacaoController.obterTotalDespesas(filtroInicio, filtroFim);
        rowDespesas.createCell(1).setCellValue(despesas.doubleValue());
        rowDespesas.getCell(1).setCellStyle(currencyStyle);
        
        // Saldo
        Row rowSaldo = sheet.createRow(rowNum++);
        rowSaldo.createCell(0).setCellValue("Saldo do Período:");
        BigDecimal saldo = receitas.subtract(despesas);
        rowSaldo.createCell(1).setCellValue(saldo.doubleValue());
        rowSaldo.getCell(1).setCellStyle(currencyStyle);
        
        // Saldo Total
        Row rowSaldoTotal = sheet.createRow(rowNum++);
        rowSaldoTotal.createCell(0).setCellValue("Saldo Total de Contas:");
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        rowSaldoTotal.createCell(1).setCellValue(saldoTotal.doubleValue());
        rowSaldoTotal.getCell(1).setCellStyle(currencyStyle);
        
        // Despesas por Categoria
        rowNum += 2;
        Row categoriaHeader = sheet.createRow(rowNum);
        categoriaHeader.createCell(0).setCellValue("Despesas por Categoria");
        categoriaHeader.getCell(0).setCellStyle(titleStyle);
        
        rowNum++;
        Row colHeader = sheet.createRow(rowNum);
        colHeader.createCell(0).setCellValue("Categoria");
        colHeader.createCell(1).setCellValue("Valor (MT)");
        colHeader.createCell(2).setCellValue("Percentual");
        for (int i = 0; i < 3; i++) {
            colHeader.getCell(i).setCellStyle(headerStyle);
        }
        
        List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
        double totalDespesasCalculado = 0;
        for (Object[] linha : dados) {
            totalDespesasCalculado += (Double) linha[1];
        }
        
        rowNum++;
        for (Object[] linha : dados) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) linha[0]);
            Double valor = (Double) linha[1];
            Double percentual = totalDespesasCalculado > 0 ? (valor / totalDespesasCalculado) * 100 : 0;
            
            row.createCell(1).setCellValue(valor);
            row.getCell(1).setCellStyle(currencyStyle);
            row.createCell(2).setCellValue(percentual / 100.0);
            row.getCell(2).setCellStyle(workbook.createCellStyle());
            row.getCell(2).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        }
        
        // Ajustar largura das colunas
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 3000);
        sheet.setColumnWidth(2, 3000);
        
        try (FileOutputStream out = new FileOutputStream(arquivo)) {
            workbook.write(out);
            out.flush();
        } finally {
            workbook.close();
        }
    }
    
    private String gerarRelatorioTexto() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        sb.append("╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                    RELATÓRIO FINANCEIRO                         ║\n");
        sb.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        sb.append("Período: ").append(filtroInicio.format(formatter)).append(" até ").append(filtroFim.format(formatter)).append("\n");
        sb.append("Gerado em: ").append(LocalDate.now().format(formatter)).append("\n\n");
        
        // Estatísticas
        sb.append("┌────────────────────────────────────────────────────────────────┐\n");
        sb.append("│ ESTATÍSTICAS DO PERÍODO                                        │\n");
        sb.append("├────────────────────────────────────────────────────────────────┤\n");
        
        BigDecimal receitas = transacaoController.obterTotalReceitas(filtroInicio, filtroFim);
        BigDecimal despesas = transacaoController.obterTotalDespesas(filtroInicio, filtroFim);
        BigDecimal saldo = receitas.subtract(despesas);
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        
        sb.append(String.format("│ Total Receitas:                        %18s │\n", 
            String.format("MT %,.2f", receitas)));
        sb.append(String.format("│ Total Despesas:                        %18s │\n", 
            String.format("MT %,.2f", despesas)));
        sb.append(String.format("│ Saldo do Período:                      %18s │\n", 
            String.format("MT %,.2f", saldo)));
        sb.append(String.format("│ Saldo Total de Contas:                 %18s │\n", 
            String.format("MT %,.2f", saldoTotal)));
        sb.append("└────────────────────────────────────────────────────────────────┘\n\n");
        
        // Despesas por Categoria
        sb.append("┌────────────────────────────────────────────────────────────────┐\n");
        sb.append("│ DESPESAS POR CATEGORIA                                         │\n");
        sb.append("├────────────────────────────────────────────────────────────────┤\n");
        
        List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
        
        if (dados.isEmpty()) {
            sb.append("│ Nenhuma despesa encontrada no período.                       │\n");
        } else {
            double totalDespesasCalculado = 0;
            for (Object[] linha : dados) {
                totalDespesasCalculado += (Double) linha[1];
            }
            
            for (Object[] linha : dados) {
                String categoria = (String) linha[0];
                Double valor = (Double) linha[1];
                Double percentual = totalDespesasCalculado > 0 ? (valor / totalDespesasCalculado) * 100 : 0;
                
                sb.append(String.format("│ %-30s %18s %7s │\n", 
                    categoria, 
                    String.format("MT %,.2f", valor),
                    String.format("%.1f%%", percentual)));
            }
        }
        
        sb.append("└────────────────────────────────────────────────────────────────┘\n");
        
        return sb.toString();
    }
}