package view.telas;

import view.telas.componentes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuPrincipal extends JFrame {
    
    // Controllers (serão injetados)
    private Integer usuarioId;
    
    // Sistema de cards
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Cards (telas)
    private DashboardCard dashboardCard;
    private ContasCard contasCard;
    private CategoriasCard categoriasCard;
    private TransacoesCard transacoesCard;
    private RelatoriosCard relatoriosCard;
    
    // Menu
    private JMenuBar menuBar;
    
    public MenuPrincipal(Integer usuarioId) {
        this.usuarioId = usuarioId;
        initComponents();
        setupMenu();
        setupCards();
        setupKeyboardShortcuts();
    }
    
    private void initComponents() {
        setTitle("Sistema de Gestão Financeira - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 700));
        
        // Configurar layout principal
        setLayout(new BorderLayout());
        
        // Criar painel de cards
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Criar as telas (cards) com o usuário ID
        dashboardCard = new DashboardCard(usuarioId, this);
        contasCard = new ContasCard(usuarioId, this);
        categoriasCard = new CategoriasCard(usuarioId, this);
        transacoesCard = new TransacoesCard(usuarioId, this);
        relatoriosCard = new RelatoriosCard(usuarioId, this);
        
        // Adicionar cards ao painel
        cardPanel.add(dashboardCard, "DASHBOARD");
        cardPanel.add(contasCard, "CONTAS");
        cardPanel.add(categoriasCard, "CATEGORIAS");
        cardPanel.add(transacoesCard, "TRANSAÇÕES");
        cardPanel.add(relatoriosCard, "RELATORIOS");
        
        // Adicionar ao frame
        add(cardPanel, BorderLayout.CENTER);
        
        // Centralizar na tela
        pack();
        setLocationRelativeTo(null);
    }
    
    private void setupMenu() {
        menuBar = new JMenuBar();
        
        // Menu Cadastros
        JMenu menuCadastros = new JMenu("Cadastros");
        menuCadastros.setMnemonic(KeyEvent.VK_C);
        
        JMenuItem itemDashboard = new JMenuItem("Dashboard");
        JMenuItem itemContas = new JMenuItem("Contas");
        JMenuItem itemCategorias = new JMenuItem("Categorias");
        JMenuItem itemTransacoes = new JMenuItem("Transações");
        
        itemDashboard.addActionListener(e -> mostrarTela("DASHBOARD"));
        itemContas.addActionListener(e -> mostrarTela("CONTAS"));
        itemCategorias.addActionListener(e -> mostrarTela("CATEGORIAS"));
        itemTransacoes.addActionListener(e -> mostrarTela("TRANSAÇÕES"));
        
        menuCadastros.add(itemDashboard);
        menuCadastros.addSeparator();
        menuCadastros.add(itemContas);
        menuCadastros.add(itemCategorias);
        menuCadastros.add(itemTransacoes);
        
        // Menu Relatórios
        JMenu menuRelatorios = new JMenu("Relatórios");
        menuRelatorios.setMnemonic(KeyEvent.VK_R);
        
        JMenuItem itemRelatorios = new JMenuItem("Relatórios");
        itemRelatorios.addActionListener(e -> mostrarTela("RELATORIOS"));
        
        menuRelatorios.add(itemRelatorios);
        
        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        menuAjuda.setMnemonic(KeyEvent.VK_A);
        
        JMenuItem itemSobre = new JMenuItem("Sobre");
        JMenuItem itemSair = new JMenuItem("Sair");
        
        itemSobre.addActionListener(e -> mostrarSobre());
        itemSair.addActionListener(e -> sair());
        
        menuAjuda.add(itemSobre);
        menuAjuda.addSeparator();
        menuAjuda.add(itemSair);
        
        // Adicionar menus à barra
        menuBar.add(menuCadastros);
        menuBar.add(menuRelatorios);
        menuBar.add(Box.createHorizontalGlue()); // Empurra para a direita
        menuBar.add(menuAjuda);
        
        setJMenuBar(menuBar);
    }
    
    private void setupCards() {
        // Configurar callbacks para atualização
        contasCard.setOnUpdateCallback(() -> {
            dashboardCard.atualizarDados();
        });
        
        categoriasCard.setOnUpdateCallback(() -> {
            transacoesCard.recarregarCategorias();
        });
        
        transacoesCard.setOnUpdateCallback(() -> {
            dashboardCard.atualizarDados();
        });
    }
    
    private void setupKeyboardShortcuts() {
        // Atalhos de teclado
        InputMap inputMap = cardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = cardPanel.getActionMap();
        
        // Ctrl+1: Dashboard
        inputMap.put(KeyStroke.getKeyStroke("control 1"), "showDashboard");
        actionMap.put("showDashboard", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("DASHBOARD");
            }
        });
        
        // Ctrl+2: Contas
        inputMap.put(KeyStroke.getKeyStroke("control 2"), "showContas");
        actionMap.put("showContas", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("CONTAS");
            }
        });
        
        // Ctrl+3: Transações
        inputMap.put(KeyStroke.getKeyStroke("control 3"), "showTransacoes");
        actionMap.put("showTransacoes", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("TRANSAÇÕES");
            }
        });
        
        // Ctrl+Q: Sair
        inputMap.put(KeyStroke.getKeyStroke("control Q"), "sair");
        actionMap.put("sair", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                sair();
            }
        });
    }
    
    // Métodos públicos para navegação
    public void mostrarTela(String tela) {
        cardLayout.show(cardPanel, tela);
        atualizarTitulo(tela);
        
        // Atualizar dados da tela específica
        switch (tela) {
            case "DASHBOARD":
                dashboardCard.atualizarDados();
                break;
            case "CONTAS":
                contasCard.carregarDados();
                break;
            case "CATEGORIAS":
                categoriasCard.carregarDados();
                break;
            case "TRANSAÇÕES":
                transacoesCard.carregarDados();
                break;
            case "RELATORIOS":
                relatoriosCard.carregarDados();
                break;
        }
    }
    
    private void atualizarTitulo(String tela) {
        String titulo = "Sistema de Gestão Financeira - ";
        switch (tela) {
            case "DASHBOARD":
                titulo += "Dashboard";
                break;
            case "CONTAS":
                titulo += "Gerenciar Contas";
                break;
            case "CATEGORIAS":
                titulo += "Gerenciar Categorias";
                break;
            case "TRANSAÇÕES":
                titulo += "Gerenciar Transações";
                break;
            case "RELATORIOS":
                titulo += "Relatórios";
                break;
        }
        setTitle(titulo);
    }
    
    public void mostrarFormularioNovaConta() {
        mostrarTela("CONTAS");
        contasCard.mostrarFormularioNovaConta();
    }
    
    public void mostrarFormularioNovaTransacao() {
        mostrarTela("TRANSAÇÕES");
        transacoesCard.mostrarAbaCadastro();
    }
    
    private void mostrarSobre() {
        JOptionPane.showMessageDialog(this,
            "Sistema de Gestão Financeira v1.0\n" +
            "Desenvolvido com Java Swing\n" +
            "Arquitetura MVC\n" +
            "© 2024 - Todos os direitos reservados",
            "Sobre o Sistema",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    // Getters para os cards (se necessário)
    public DashboardCard getDashboardCard() {
        return dashboardCard;
    }
    
    public ContasCard getContasCard() {
        return contasCard;
    }
    
    public TransacoesCard getTransacoesCard() {
        return transacoesCard;
    }
}