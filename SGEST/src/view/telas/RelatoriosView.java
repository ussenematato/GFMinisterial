package view.telas;

import controller.TransacaoController;
import controller.ContaController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class RelatoriosView extends JDialog {
    private Integer usuarioId;
    private TransacaoController transacaoController;
    private ContaController contaController;
    
    public RelatoriosView(Integer usuarioId) {
        this.usuarioId = usuarioId;
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaController = new ContaController(usuarioId);
        
        initComponents();
        carregarRelatorios();
    }
    
    private void initComponents() {
        setTitle("Relatórios - Gestão Financeira");
        setLayout(new BorderLayout(10, 10));
        setModal(true);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Aba 1: Resumo Geral
        tabbedPane.addTab("Resumo Geral", criarPainelResumoGeral());
        
        // Aba 2: Despesas por Categoria
        tabbedPane.addTab("Despesas por Categoria", criarPainelDespesasCategoria());
        
        // Aba 3: Evolução Mensal
        tabbedPane.addTab("Evolução Mensal", criarPainelEvolucaoMensal());
        
        // Aba 4: Detalhamento
        tabbedPane.addTab("Detalhamento", criarPainelDetalhamento());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnImprimir = new JButton("Imprimir");
        JButton btnFechar = new JButton("Fechar");
        
        btnImprimir.setBackground(new Color(70, 130, 180));
        btnImprimir.setForeground(Color.WHITE);
        btnFechar.setBackground(new Color(169, 169, 169));
        btnFechar.setForeground(Color.WHITE);
        
        btnFechar.addActionListener(e -> dispose());
        
        panelBotoes.add(btnImprimir);
        panelBotoes.add(btnFechar);
        add(panelBotoes, BorderLayout.SOUTH);
        
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }
    
    private JPanel criarPainelResumoGeral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Período do mês atual
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        // Obter dados
        BigDecimal receitas = transacaoController.obterTotalReceitas(inicio, fim);
        BigDecimal despesas = transacaoController.obterTotalDespesas(inicio, fim);
        BigDecimal saldoMes = receitas.subtract(despesas);
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        
        // Criar dataset para gráfico de pizza
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Receitas", receitas.doubleValue());
        dataset.setValue("Despesas", despesas.doubleValue());
        
        // Criar gráfico
        JFreeChart chart = ChartFactory.createPieChart(
            "Distribuição Financeira do Mês",
            dataset,
            true, // legenda
            true, // tooltips
            false // URLs
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        
        // Painel de estatísticas
        JPanel panelStats = new JPanel(new GridLayout(4, 2, 10, 10));
        panelStats.setBorder(BorderFactory.createTitledBorder("Estatísticas do Mês"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/yyyy");
        String mesAno = mesAtual.format(formatter);
        
        panelStats.add(new JLabel("Período:"));
        panelStats.add(new JLabel(mesAno));
        
        panelStats.add(new JLabel("Total Receitas:"));
        JLabel lblReceitas = new JLabel(String.format("R$ %,.2f", receitas));
        lblReceitas.setForeground(new Color(0, 150, 0));
        lblReceitas.setFont(new Font("Arial", Font.BOLD, 12));
        panelStats.add(lblReceitas);
        
        panelStats.add(new JLabel("Total Despesas:"));
        JLabel lblDespesas = new JLabel(String.format("R$ %,.2f", despesas));
        lblDespesas.setForeground(new Color(200, 0, 0));
        lblDespesas.setFont(new Font("Arial", Font.BOLD, 12));
        panelStats.add(lblDespesas);
        
        panelStats.add(new JLabel("Saldo do Mês:"));
        JLabel lblSaldoMes = new JLabel(String.format("R$ %,.2f", saldoMes));
        if (saldoMes.compareTo(BigDecimal.ZERO) >= 0) {
            lblSaldoMes.setForeground(new Color(0, 150, 0));
        } else {
            lblSaldoMes.setForeground(new Color(200, 0, 0));
        }
        lblSaldoMes.setFont(new Font("Arial", Font.BOLD, 14));
        panelStats.add(lblSaldoMes);
        
        // Saldo total
        JPanel panelSaldoTotal = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSaldoTotal.setBorder(BorderFactory.createTitledBorder("Saldo Total"));
        JLabel lblSaldoTotal = new JLabel(String.format("R$ %,.2f", saldoTotal));
        lblSaldoTotal.setFont(new Font("Arial", Font.BOLD, 24));
        if (saldoTotal.compareTo(BigDecimal.ZERO) >= 0) {
            lblSaldoTotal.setForeground(new Color(0, 100, 0));
        } else {
            lblSaldoTotal.setForeground(new Color(200, 0, 0));
        }
        panelSaldoTotal.add(lblSaldoTotal);
        
        // Layout
        JPanel panelEsquerda = new JPanel(new BorderLayout());
        panelEsquerda.add(chartPanel, BorderLayout.CENTER);
        
        JPanel panelDireita = new JPanel(new BorderLayout());
        panelDireita.add(panelStats, BorderLayout.NORTH);
        panelDireita.add(panelSaldoTotal, BorderLayout.CENTER);
        
        panel.add(panelEsquerda, BorderLayout.CENTER);
        panel.add(panelDireita, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel criarPainelDespesasCategoria() {
        JPanel panel = new JPanel(new BorderLayout());
        
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        List<Object[]> dados = transacaoController.obterDespesasPorCategoria(inicio, fim);
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Object[] linha : dados) {
            String categoria = (String) linha[0];
            Double valor = (Double) linha[1];
            dataset.setValue(categoria, valor);
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Despesas por Categoria - " + mesAtual.format(DateTimeFormatter.ofPattern("MMMM/yyyy")),
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
                String.format("R$ %,.2f", valor),
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
        YearMonth mesAtual = YearMonth.now();
        for (int i = 5; i >= 0; i--) {
            YearMonth mes = mesAtual.minusMonths(i);
            LocalDate inicio = mes.atDay(1);
            LocalDate fim = mes.atEndOfMonth();
            
            BigDecimal receitas = transacaoController.obterTotalReceitas(inicio, fim);
            BigDecimal despesas = transacaoController.obterTotalDespesas(inicio, fim);
            
            dataset.addValue(receitas, "Receitas", mes.format(DateTimeFormatter.ofPattern("MM/yy")));
            dataset.addValue(despesas, "Despesas", mes.format(DateTimeFormatter.ofPattern("MM/yy")));
        }
        
        JFreeChart chart = ChartFactory.createLineChart(
            "Evolução Financeira - Últimos 6 Meses",
            "Mês",
            "Valor (R$)",
            dataset
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelDetalhamento() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea txtDetalhes = new JTextArea();
        txtDetalhes.setEditable(false);
        txtDetalhes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO DETALHADO - ").append(mesAtual.format(DateTimeFormatter.ofPattern("MMMM/yyyy"))).append("\n");
        sb.append("=".repeat(60)).append("\n\n");
        
        // Receitas
        sb.append("RECEITAS:\n");
        sb.append("-".repeat(40)).append("\n");
        
        List<Object[]> despesasPorCategoria = transacaoController.obterDespesasPorCategoria(inicio, fim);
        BigDecimal totalDespesas = BigDecimal.ZERO;
        
        for (Object[] linha : despesasPorCategoria) {
            String categoria = (String) linha[0];
            Double valor = (Double) linha[1];
            sb.append(String.format("%-20s R$ %12.2f\n", categoria, valor));
            totalDespesas = totalDespesas.add(BigDecimal.valueOf(valor));
        }
        
        sb.append("\n");
        sb.append(String.format("%-20s R$ %12.2f\n", "TOTAL DESPESAS:", totalDespesas));
        
        txtDetalhes.setText(sb.toString());
        
        JScrollPane scrollPane = new JScrollPane(txtDetalhes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void carregarRelatorios() {
        // Os relatórios são carregados automaticamente na criação de cada painel
    }
}