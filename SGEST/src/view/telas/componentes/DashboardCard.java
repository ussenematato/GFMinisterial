package view.telas.componentes;

import controller.ContaController;
import controller.TransacaoController;
import model.entity.Conta;
import model.entity.Transacao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import view.telas.MenuPrincipal;

public class DashboardCard extends CardBase {
    
    private ContaController contaController;
    private TransacaoController transacaoController;
    
    // Componentes
    private JLabel lblSaldoTotal;
    private JLabel lblReceitasMes;
    private JLabel lblDespesasMes;
    private JLabel lblSaldoMes;
    private JTable tblUltimasTransacoes;
    private JTable tblContas;
    private DefaultTableModel modelTransacoes;
    private DefaultTableModel modelContas;
    
    public DashboardCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.contaController = new ContaController(usuarioId);
        this.transacaoController = new TransacaoController(usuarioId);
        initComponents();
        atualizarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior - Cards de resumo
        JPanel panelResumo = criarPanelResumo();
        
        // Painel central - Grid com contas e transações
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Painel de Contas
        JPanel panelContas = criarPanelContas();
        
        // Painel de Transações Recentes
        JPanel panelTransacoes = criarPanelTransacoes();
        
        panelCentral.add(panelContas);
        panelCentral.add(panelTransacoes);
        
        // Painel inferior - Botões rápidos
        JPanel panelBotoesRapidos = criarPanelBotoesRapidos();
        
        // Adicionando componentes
        add(panelResumo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotoesRapidos, BorderLayout.SOUTH);
    }
    
    private JPanel criarPanelResumo() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Resumo Financeiro"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(245, 245, 245));
        
        // Card Saldo Total
        JPanel cardSaldoTotal = criarCardResumo("Saldo Total", "R$ 0,00", new Color(46, 125, 50));
        lblSaldoTotal = (JLabel) ((JPanel) cardSaldoTotal.getComponent(1)).getComponent(0);
        
        // Card Receitas Mês
        JPanel cardReceitasMes = criarCardResumo("Receitas do Mês", "R$ 0,00", new Color(30, 136, 229));
        lblReceitasMes = (JLabel) ((JPanel) cardReceitasMes.getComponent(1)).getComponent(0);
        
        // Card Despesas Mês
        JPanel cardDespesasMes = criarCardResumo("Despesas do Mês", "R$ 0,00", new Color(229, 57, 53));
        lblDespesasMes = (JLabel) ((JPanel) cardDespesasMes.getComponent(1)).getComponent(0);
        
        // Card Saldo Mês
        JPanel cardSaldoMes = criarCardResumo("Saldo do Mês", "R$ 0,00", new Color(121, 85, 72));
        lblSaldoMes = (JLabel) ((JPanel) cardSaldoMes.getComponent(1)).getComponent(0);
        
        // Adicionar cards ao painel
        panel.add(cardSaldoTotal);
        panel.add(cardReceitasMes);
        panel.add(cardDespesasMes);
        panel.add(cardSaldoMes);
        
        return panel;
    }
    
    private JPanel criarCardResumo(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor.brighter(), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(Color.WHITE);
        
        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(Color.DARK_GRAY);
        
        // Valor
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(cor);
        
        JPanel panelValor = new JPanel(new BorderLayout());
        panelValor.setBackground(Color.WHITE);
        panelValor.add(lblValor, BorderLayout.CENTER);
        
        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(panelValor, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarPanelContas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Contas Bancárias"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Tipo", "Saldo", "Instituição"};
        modelContas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblContas = new JTable(modelContas);
        tblContas.setRowHeight(30);
        tblContas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblContas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Renderer personalizado para saldo (sem usar a classe Renderers)
        tblContas.getColumnModel().getColumn(2).setCellRenderer(new SaldoCellRendererCustom());
        
        JScrollPane scrollPane = new JScrollPane(tblContas);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        
        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnNovaConta = criarBotao("Nova Conta", new Color(30, 136, 229));
        JButton btnVerTodas = criarBotao("Ver Todas", new Color(96, 125, 139));
        
        panelBotoes.add(btnNovaConta);
        panelBotoes.add(btnVerTodas);
        
        // Ações dos botões
        btnNovaConta.addActionListener(e -> {
            if (menuPrincipal != null) {
                menuPrincipal.mostrarFormularioNovaConta();
            }
        });
        
        btnVerTodas.addActionListener(e -> {
            if (menuPrincipal != null) {
                menuPrincipal.mostrarTela("CONTAS");
            }
        });
        
        // Adicionando ao painel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotoes, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Classe interna para renderizar saldo
    private class SaldoCellRendererCustom extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal saldo = (BigDecimal) value;
                setText(String.format("R$ %,.2f", saldo));
                
                if (saldo.compareTo(BigDecimal.ZERO) >= 0) {
                    setForeground(new Color(46, 125, 50));
                } else {
                    setForeground(new Color(229, 57, 53));
                }
                
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
    
    private JPanel criarPanelTransacoes() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Últimas Transações"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Modelo da tabela
        String[] colunas = {"Data", "Descrição", "Valor", "Status"};
        modelTransacoes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblUltimasTransacoes = new JTable(modelTransacoes);
        tblUltimasTransacoes.setRowHeight(30);
        tblUltimasTransacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblUltimasTransacoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Renderer personalizado para valor (sem usar a classe Renderers)
        tblUltimasTransacoes.getColumnModel().getColumn(2).setCellRenderer(new ValorCellRendererCustom());
        
        JScrollPane scrollPane = new JScrollPane(tblUltimasTransacoes);
        
        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnNovaTransacao = criarBotao("Nova Transação", new Color(30, 136, 229));
        JButton btnVerTodas = criarBotao("Ver Todas", new Color(96, 125, 139));
        
        panelBotoes.add(btnNovaTransacao);
        panelBotoes.add(btnVerTodas);
        
        // Ações dos botões
        btnNovaTransacao.addActionListener(e -> {
            if (menuPrincipal != null) {
                menuPrincipal.mostrarFormularioNovaTransacao();
            }
        });
        
        btnVerTodas.addActionListener(e -> {
            if (menuPrincipal != null) {
                menuPrincipal.mostrarTela("TRANSAÇÕES");
            }
        });
        
        // Adicionando ao painel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotoes, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Classe interna para renderizar valor
    private class ValorCellRendererCustom extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal valor = (BigDecimal) value;
                setText(String.format("R$ %,.2f", valor));
                
                // Determinar cor baseado no tipo (despesa ou receita)
                try {
                    String status = (String) table.getValueAt(row, 3); // Coluna Status
                    if (status != null) {
                        if ("DESPESA".equals(status.toUpperCase())) {
                            setForeground(new Color(229, 57, 53));
                        } else {
                            setForeground(new Color(46, 125, 50));
                        }
                    }
                } catch (Exception e) {
                    // Se não conseguir determinar, usa cor padrão
                }
                
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
    
    private JPanel criarPanelBotoesRapidos() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Botões com ícones
        JButton btnContas = criarBotao("Contas", new Color(30, 136, 229));
        JButton btnCategorias = criarBotao("Categorias", new Color(156, 39, 176));
        JButton btnTransacoes = criarBotao("Transações", new Color(255, 152, 0));
        JButton btnRelatorios = criarBotao("Relatórios", new Color(76, 175, 80));
        
        // Ações
        btnContas.addActionListener(e -> menuPrincipal.mostrarTela("CONTAS"));
        btnCategorias.addActionListener(e -> menuPrincipal.mostrarTela("CATEGORIAS"));
        btnTransacoes.addActionListener(e -> menuPrincipal.mostrarTela("TRANSAÇÕES"));
        btnRelatorios.addActionListener(e -> menuPrincipal.mostrarTela("RELATORIOS"));
        
        panel.add(btnContas);
        panel.add(btnCategorias);
        panel.add(btnTransacoes);
        panel.add(btnRelatorios);
        
        return panel;
    }
    
    @Override
    public void carregarDados() {
        atualizarDados();
    }
    
    public void atualizarDados() {
        // Atualizar resumo
        atualizarResumo();
        
        // Atualizar tabelas
        carregarContas();
        carregarUltimasTransacoes();
    }
    
    private void atualizarResumo() {
        // Saldo total
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        lblSaldoTotal.setText(formatarMoeda(saldoTotal));
        
        // Período do mês atual
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        BigDecimal receitas = transacaoController.obterTotalReceitas(inicio, fim);
        BigDecimal despesas = transacaoController.obterTotalDespesas(inicio, fim);
        BigDecimal saldoMes = receitas.subtract(despesas);
        
        lblReceitasMes.setText(formatarMoeda(receitas));
        lblDespesasMes.setText(formatarMoeda(despesas));
        lblSaldoMes.setText(formatarMoeda(saldoMes));
        
        // Cor do saldo do mês
        if (saldoMes.compareTo(BigDecimal.ZERO) >= 0) {
            lblSaldoMes.setForeground(new Color(46, 125, 50));
        } else {
            lblSaldoMes.setForeground(new Color(229, 57, 53));
        }
    }
    
    private void carregarContas() {
        modelContas.setRowCount(0);
        List<Conta> contas = contaController.listarContasAtivas();
        
        for (Conta conta : contas) {
            Object[] linha = {
                conta.getNome(),
                formatarTipoConta(conta.getTipo()),
                conta.getSaldoAtual(),
                conta.getInstituicao()
            };
            modelContas.addRow(linha);
        }
    }
    
    private void carregarUltimasTransacoes() {
        modelTransacoes.setRowCount(0);
        
        // Últimos 30 dias
        LocalDate fim = LocalDate.now();
        LocalDate inicio = fim.minusDays(30);
        
        List<Transacao> transacoes = transacaoController.listarTransacoesPeriodo(inicio, fim);
        
        // Limitar a 10 transações
        int limite = Math.min(transacoes.size(), 10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        
        for (int i = 0; i < limite; i++) {
            Transacao t = transacoes.get(i);
            Object[] linha = {
                t.getDataTransacao().format(formatter),
                t.getDescricao(),
                t.getValor(),
                t.getTipo()
            };
            modelTransacoes.addRow(linha);
        }
    }
    
    private String formatarMoeda(BigDecimal valor) {
        return String.format("R$ %,.2f", valor);
    }
    
    private String formatarTipoConta(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "CORRENTE" -> "Conta Corrente";
            case "POUPANCA" -> "Poupança";
            case "INVESTIMENTO" -> "Investimento";
            case "CARTEIRA" -> "Carteira";
            default -> tipo;
        };
    }
}