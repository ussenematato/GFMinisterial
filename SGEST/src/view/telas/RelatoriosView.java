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
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.text.MessageFormat;
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
    private boolean mostrarTodos = true; // Mostrar dados de todos os usuários
    
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
    private JTable tabelaContas;
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
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        
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
        
        util.UIStyler.styleSuccessButton(btnExportar);
        util.UIStyler.styleSecondaryButton(btnImprimir);
        util.UIStyler.styleNeutralButton(btnFechar);
        
        btnExportar.addActionListener(e -> exportarParaExcel());
        btnImprimir.addActionListener(e -> imprimir());
        btnFechar.addActionListener(e -> dispose());
        
        panelBotoes.add(btnExportar);
        panelBotoes.add(btnImprimir);
        panelBotoes.add(btnFechar);
        add(panelBotoes, BorderLayout.SOUTH);
        
        setSize(1100, 700);
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
        
        // Painel superior com texto
        JPanel panelTexto = new JPanel(new BorderLayout());
        txtDetalhamentoCompleto = new JTextArea();
        txtDetalhamentoCompleto.setEditable(false);
        txtDetalhamentoCompleto.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 11));
        
        JScrollPane scrollTexto = new JScrollPane(txtDetalhamentoCompleto);
        scrollTexto.setPreferredSize(new Dimension(800, 300));
        panelTexto.add(scrollTexto, BorderLayout.CENTER);
        
        // Painel inferior com tabela de contas
        JPanel panelContas = new JPanel(new BorderLayout());
        panelContas.setBorder(BorderFactory.createTitledBorder("Saldo das Contas"));
        
        // Criar tabela de contas
        String[] colunasContas = {"ID", "Conta", "Tipo", "Instituição", "Saldo Atual"};
        DefaultTableModel modeloContas = new DefaultTableModel(colunasContas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        this.tabelaContas = new JTable(modeloContas);
        tabelaContas.setFont(new Font("Arial", Font.PLAIN, 11));
        tabelaContas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tabelaContas.setRowHeight(25);
        
        // Carregar dados de contas
        List<model.entity.Conta> contas = contaController.listarContasAtivas();
        for (model.entity.Conta conta : contas) {
            Object[] row = {
                conta.getId(),
                conta.getNome(),
                conta.getTipo(),
                conta.getInstituicao(),
                CurrencyUtils.formatCurrency(conta.getSaldoAtual())
            };
            modeloContas.addRow(row);
        }
        
        JScrollPane scrollContas = new JScrollPane(tabelaContas);
        scrollContas.setPreferredSize(new Dimension(800, 150));
        panelContas.add(scrollContas, BorderLayout.CENTER);
        
        // Adicionar ao painel principal
        panel.add(panelTexto, BorderLayout.CENTER);
        panel.add(panelContas, BorderLayout.SOUTH);
        
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
        
        List<Object[]> receitas = mostrarTodos 
            ? transacaoController.obterTodasReceitasPorCategoria(dataInicio, dataFim)
            : transacaoController.obterReceitasPorCategoria(dataInicio, dataFim);
        BigDecimal totalReceitas = BigDecimal.ZERO;
        
        if (receitas.isEmpty()) {
            sb.append("│ Sem receitas registradas neste período                                    │\n");
        } else {
            for (Object[] linha : receitas) {
                String categoria = (String) linha[0];
                Double valor = (Double) linha[1];
                BigDecimal bdValor = BigDecimal.valueOf(valor);
                totalReceitas = totalReceitas.add(bdValor);
                
                sb.append(String.format("│ %-40s %15s │\n", 
                    categoria, 
                    CurrencyUtils.formatCurrency(bdValor)));
            }
        }
        
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ %-40s %15s │\n", 
            "TOTAL RECEITAS:",
            CurrencyUtils.formatCurrency(totalReceitas)));
        sb.append("└────────────────────────────────────────────────────────────────────────────┘\n\n");
        
        // DESPESAS
        sb.append("┌────────────────────────────────────────────────────────────────────────────┐\n");
        sb.append("│ DESPESAS                                                                   │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        
        List<Object[]> despesas = mostrarTodos 
            ? transacaoController.obterTodasDespesasPorCategoria(dataInicio, dataFim)
            : transacaoController.obterDespesasPorCategoria(dataInicio, dataFim);
        BigDecimal totalDespesas = BigDecimal.ZERO;
        
        if (despesas.isEmpty()) {
            sb.append("│ Sem despesas registradas neste período                                   │\n");
        } else {
            for (Object[] linha : despesas) {
                String categoria = (String) linha[0];
                Double valor = (Double) linha[1];
                BigDecimal bdValor = BigDecimal.valueOf(valor);
                totalDespesas = totalDespesas.add(bdValor);
                
                sb.append(String.format("│ %-40s %15s │\n", 
                    categoria,
                    CurrencyUtils.formatCurrency(bdValor)));
            }
        }
        
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ %-40s %15s │\n", 
            "TOTAL DESPESAS:",
            CurrencyUtils.formatCurrency(totalDespesas)));
        sb.append("└────────────────────────────────────────────────────────────────────────────┘\n\n");
        
        // SALDO DAS CONTAS
        sb.append("┌────────────────────────────────────────────────────────────────────────────┐\n");
        sb.append("│ SALDO DAS CONTAS                                                           │\n");
        sb.append("├────────────────────────────────────────────────────────────────────────────┤\n");
        
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        BigDecimal saldoPeriodo = totalReceitas.subtract(totalDespesas);
        
        sb.append(String.format("│ %-40s %15s │\n", 
            "Saldo do Período:",
            CurrencyUtils.formatCurrency(saldoPeriodo)));
        sb.append(String.format("│ %-40s %15s │\n", 
            "Saldo Total (Contas):",
            CurrencyUtils.formatCurrency(saldoTotal)));
        
        sb.append("└────────────────────────────────────────────────────────────────────────────┘\n");
        
        txtDetalhamentoCompleto.setText(sb.toString());
        txtDetalhamentoCompleto.setCaretPosition(0);
    }
    
    private JPanel criarPainelResumoGeral() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Obter dados - usando métodos que retornam TODOS os dados
        BigDecimal receitas = mostrarTodos 
            ? transacaoController.obterTotalTodasReceitas(dataInicio, dataFim)
            : transacaoController.obterTotalReceitas(dataInicio, dataFim);
        BigDecimal despesas = mostrarTodos 
            ? transacaoController.obterTotalTodasDespesas(dataInicio, dataFim)
            : transacaoController.obterTotalDespesas(dataInicio, dataFim);
        BigDecimal saldoMes = receitas.subtract(despesas);
        BigDecimal saldoTotal = mostrarTodos 
            ? contaController.obterSaldoTotalTodas()
            : contaController.obterSaldoTotal();
        
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
        chartPanel.setPreferredSize(new Dimension(600, 350));
        
        // Painel de estatísticas - Layout melhorado
        JPanel panelStats = new JPanel(new GridBagLayout());
        panelStats.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Resumo do Período"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 1;
        
        // Receitas
        JLabel lblReceitas = new JLabel("Total Receitas:");
        lblReceitas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelStats.add(lblReceitas, gbc);
        
        JLabel valReceitas = new JLabel(CurrencyUtils.formatCurrency(receitas));
        valReceitas.setForeground(new java.awt.Color(0, 150, 0));
        valReceitas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        gbc.gridx = 1;
        panelStats.add(valReceitas, gbc);
        
        // Despesas
        JLabel lblDespesas = new JLabel("Total Despesas:");
        lblDespesas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelStats.add(lblDespesas, gbc);
        
        JLabel valDespesas = new JLabel(CurrencyUtils.formatCurrency(despesas));
        valDespesas.setForeground(new java.awt.Color(200, 0, 0));
        valDespesas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        gbc.gridx = 1;
        panelStats.add(valDespesas, gbc);
        
        // Separador
        JSeparator sep1 = new JSeparator(JSeparator.HORIZONTAL);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelStats.add(sep1, gbc);
        gbc.gridwidth = 1;
        
        // Saldo do período
        JLabel lblSaldoMes = new JLabel("Saldo do Período:");
        lblSaldoMes.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelStats.add(lblSaldoMes, gbc);
        
        JLabel valSaldoMes = new JLabel(CurrencyUtils.formatCurrency(saldoMes));
        if (saldoMes.compareTo(BigDecimal.ZERO) >= 0) {
            valSaldoMes.setForeground(new java.awt.Color(0, 150, 0));
        } else {
            valSaldoMes.setForeground(new java.awt.Color(200, 0, 0));
        }
        valSaldoMes.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        gbc.gridx = 1;
        panelStats.add(valSaldoMes, gbc);
        
        // Separador
        JSeparator sep2 = new JSeparator(JSeparator.HORIZONTAL);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelStats.add(sep2, gbc);
        gbc.gridwidth = 1;
        
        // Saldo total
        JLabel lblSaldoTotal = new JLabel("Saldo Total de Contas:");
        lblSaldoTotal.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelStats.add(lblSaldoTotal, gbc);
        
        JLabel valSaldoTotal = new JLabel(CurrencyUtils.formatCurrency(saldoTotal));
        valSaldoTotal.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
        if (saldoTotal.compareTo(BigDecimal.ZERO) >= 0) {
            valSaldoTotal.setForeground(new java.awt.Color(0, 100, 0));
        } else {
            valSaldoTotal.setForeground(new java.awt.Color(200, 0, 0));
        }
        gbc.gridx = 1;
        panelStats.add(valSaldoTotal, gbc);
        
        // Spacer
        gbc.gridy = 6;
        gbc.weighty = 1;
        panelStats.add(new JLabel(), gbc);
        
        // Layout - Gráfico ocupa mais espaço
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(panelStats, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel criarPainelDespesasCategoria() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        List<Object[]> dados = mostrarTodos 
            ? transacaoController.obterTodasDespesasPorCategoria(dataInicio, dataFim)
            : transacaoController.obterDespesasPorCategoria(dataInicio, dataFim);
        
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
        
        // Tabela de detalhes com melhor layout
        String[] colunas = {"Categoria", "Valor", "Percentual"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
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
        tabela.setRowHeight(28);
        tabela.setShowGrid(true);
        tabela.setGridColor(new Color(220, 220, 220));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabela.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Renderizador para valores monetários
        tabela.getColumnModel().getColumn(1).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detalhamento por Categoria"));
        
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
        String[] options = {"Detalhamento Completo", "Tabela de Contas", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(this,
                "Escolha o que deseja imprimir:",
                "Imprimir",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (escolha == 0) { // Detalhamento Completo
            try {
                boolean ok = txtDetalhamentoCompleto.print();
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Impressão cancelada ou sem sucesso.", "Imprimir", JOptionPane.WARNING_MESSAGE);
                }
            } catch (PrinterException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao imprimir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else if (escolha == 1) { // Tabela de Contas
            if (tabelaContas == null) {
                JOptionPane.showMessageDialog(this, "Tabela de contas não disponível para impressão.", "Imprimir", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                MessageFormat header = new MessageFormat("Saldo das Contas - Relatório");
                MessageFormat footer = new MessageFormat("Página {0}");
                boolean printed = tabelaContas.print(JTable.PrintMode.FIT_WIDTH, header, footer);
                if (!printed) {
                    JOptionPane.showMessageDialog(this, "Impressão cancelada ou sem sucesso.", "Imprimir", JOptionPane.WARNING_MESSAGE);
                }
            } catch (PrinterException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao imprimir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportarParaExcel() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório em Excel");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
            // Definir diretório padrão (Documents do usuário)
            String docPath = System.getProperty("user.home") + File.separator + "Documents";
            fileChooser.setCurrentDirectory(new File(docPath));
            
            int resultado = fileChooser.showSaveDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                if (!arquivo.getAbsolutePath().endsWith(".xlsx")) {
                    arquivo = new File(arquivo.getAbsolutePath() + ".xlsx");
                }
                
                // Garantir que o diretório pai existe
                File diretorioPai = arquivo.getParentFile();
                if (diretorioPai != null && !diretorioPai.exists()) {
                    diretorioPai.mkdirs();
                }
                
                criarExcel(arquivo);
                JOptionPane.showMessageDialog(this, 
                    "Relatório exportado com sucesso!\nArquivo salvo em: " + arquivo.getAbsolutePath(), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao exportar: " + ex.getMessage() + "\nVerifique permissões e espaço em disco.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
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
        
        // CONTAS
        rowNum = rowNum + 6;
        Row contasHeader = sheet.createRow(rowNum);
        contasHeader.createCell(0).setCellValue("SALDO DAS CONTAS");
        contasHeader.getCell(0).setCellStyle(titleStyle);
        
        rowNum++;
        Row colHeaderContas = sheet.createRow(rowNum);
        colHeaderContas.createCell(0).setCellValue("Conta");
        colHeaderContas.createCell(1).setCellValue("Tipo");
        colHeaderContas.createCell(2).setCellValue("Instituição");
        colHeaderContas.createCell(3).setCellValue("Saldo");
        for (int i = 0; i < 4; i++) {
            colHeaderContas.getCell(i).setCellStyle(headerStyle);
        }
        
        List<model.entity.Conta> contas = contaController.listarContasAtivas();
        rowNum++;
        for (model.entity.Conta conta : contas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(conta.getNome());
            row.createCell(1).setCellValue(conta.getTipo());
            row.createCell(2).setCellValue(conta.getInstituicao());
            row.createCell(3).setCellValue(conta.getSaldoAtual().doubleValue());
            row.getCell(3).setCellStyle(currencyStyle);
        }
        
        // Ajustar largura das colunas
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 3000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3000);
        
        // Escrever arquivo com try-with-resources para garantir fechamento adequado
        try (FileOutputStream out = new FileOutputStream(arquivo)) {
            workbook.write(out);
            out.flush();
        } finally {
            workbook.close();
        }
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
