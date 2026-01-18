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
import view.telas.RelatoriosView;

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
        
        // Painel central com tabs - Contas/Transações e Relatórios
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Aba 1: Contas e Transações (layout original)
        JPanel panelDados = criarPainelDados();
        tabbedPane.addTab("Contas e Transações", panelDados);
        
        // Aba 2: Relatórios Rápidos
        JPanel panelRelatorios = criarPainelRelatorios();
        tabbedPane.addTab("Relatórios", panelRelatorios);
        
        // Painel inferior - Botões rápidos
        JPanel panelBotoesRapidos = criarPanelBotoesRapidos();
        
        // Adicionando componentes
        add(panelResumo, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(panelBotoesRapidos, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelDados() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Painel de Contas
        JPanel panelContas = criarPanelContas();
        
        // Painel de Transações Recentes
        JPanel panelTransacoes = criarPanelTransacoes();
        
        panel.add(panelContas);
        panel.add(panelTransacoes);
        
        return panel;
    }
    
    private JPanel criarPainelRelatorios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Painel superior com instruções
        JPanel panelInfo = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("Visualize relatórios detalhados do período atual");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(Color.DARK_GRAY);
        panelInfo.add(lblInfo, BorderLayout.WEST);
        
        // Painel central com botões de ações
        JPanel panelAcoes = new JPanel(new GridLayout(4, 1, 10, 15));
        panelAcoes.setBorder(BorderFactory.createTitledBorder("Opções de Relatório"));
        
        // Botão: Visualizar Relatórios Completos
        JButton btnVisualizarRelatorios = criarBotaoRelatorio(
            "Visualizar Relatórios",
            "Abrir tela de relatórios detalhada com gráficos e análises",
            new Color(30, 136, 229)
        );
        btnVisualizarRelatorios.addActionListener(e -> abrirRelatorios());
        
        // Botão: Exportar para Excel
        JButton btnExportar = criarBotaoRelatorio(
            "Exportar para Excel",
            "Baixar os dados financeiros em formato Excel (.xlsx)",
            new Color(76, 175, 80)
        );
        btnExportar.addActionListener(e -> exportarExcel());
        
        // Botão: Imprimir
        JButton btnImprimir = criarBotaoRelatorio(
            "Imprimir Relatório",
            "Imprimir o relatório financeiro do período",
            new Color(255, 152, 0)
        );
        btnImprimir.addActionListener(e -> imprimirRelatorio());
        
        // Botão: Enviar por Email
        JButton btnEmail = criarBotaoRelatorio(
            "Enviar por Email",
            "Enviar o relatório para o seu email registrado",
            new Color(156, 39, 176)
        );
        btnEmail.addActionListener(e -> mostrarMensagemSucesso("Funcionalidade em desenvolvimento"));
        
        panelAcoes.add(btnVisualizarRelatorios);
        panelAcoes.add(btnExportar);
        panelAcoes.add(btnImprimir);
        panelAcoes.add(btnEmail);
        
        panel.add(panelInfo, BorderLayout.NORTH);
        panel.add(panelAcoes, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton criarBotaoRelatorio(String titulo, String descricao, Color cor) {
        JButton botao = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(cor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        
        botao.setLayout(new BorderLayout());
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setOpaque(false);
        
        JPanel panelConteudo = new JPanel(new GridLayout(2, 1, 0, 5));
        panelConteudo.setOpaque(false);
        panelConteudo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblDescricao = new JLabel(descricao);
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDescricao.setForeground(new Color(220, 220, 220));
        
        panelConteudo.add(lblTitulo);
        panelConteudo.add(lblDescricao);
        
        botao.add(panelConteudo, BorderLayout.CENTER);
        botao.setPreferredSize(new Dimension(400, 60));
        
        return botao;
    }
    
    private void abrirRelatorios() {
        RelatoriosView relatorios = new RelatoriosView(usuarioId);
        relatorios.setVisible(true);
    }
    
    private void exportarExcel() {
        mostrarMensagemSucesso("Relatório sendo exportado...");
        // Esta funcionalidade já está implementada em RelatoriosView
        // Aqui apenas abrimos os relatórios
        abrirRelatorios();
    }
    
    private void imprimirRelatorio() {
        mostrarMensagemSucesso("Abrindo função de impressão...");
        abrirRelatorios();
    }
    
    private JPanel criarPanelResumo() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Resumo Financeiro"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(245, 245, 245));
        
        // Card Saldo Total
        JPanel cardSaldoTotal = criarCardResumo("Saldo Total", "MT 0,00", new Color(46, 125, 50));
        lblSaldoTotal = (JLabel) ((JPanel) cardSaldoTotal.getComponent(1)).getComponent(0);
        
        // Card Receitas Mês
        JPanel cardReceitasMes = criarCardResumo("Receitas do Mês", "MT 0,00", new Color(30, 136, 229));
        lblReceitasMes = (JLabel) ((JPanel) cardReceitasMes.getComponent(1)).getComponent(0);
        
        // Card Despesas Mês
        JPanel cardDespesasMes = criarCardResumo("Despesas do Mês", "MT 0,00", new Color(229, 57, 53));
        lblDespesasMes = (JLabel) ((JPanel) cardDespesasMes.getComponent(1)).getComponent(0);
        
        // Card Saldo Mês
        JPanel cardSaldoMes = criarCardResumo("Saldo do Mês", "MT 0,00", new Color(121, 85, 72));
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
        tblContas.setShowGrid(true);
        tblContas.setGridColor(new Color(220, 220, 220));
        
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
                setText(String.format("MT %,.2f", saldo));
                
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
        tblUltimasTransacoes.setShowGrid(true);
        tblUltimasTransacoes.setGridColor(new Color(220, 220, 220));
        
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
                setText(String.format("MT %,.2f", valor));
                
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
        return String.format("MT %,.2f", valor);
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