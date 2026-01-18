package view.telas;

import view.telas.componentes.*;
import util.UIStyler;
import model.entity.Usuario;
import model.dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class MenuPrincipal extends JFrame {
    
    // Controllers (serão injetados)
    private Integer usuarioId;
    private String perfilUsuario;
    
    // Sistema de cards
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Cards (telas)
    private DashboardCard dashboardCard;
    private ContasCard contasCard;
    private CategoriasCard categoriasCard;
    private TransacoesCard transacoesCard;
    private RelatoriosCard relatoriosCard;
    private AdmiSystemCard admiSystemCard;
    private LogsCard logsCard;
    private view.telas.componentes.ConfiguracoesCard configuracoesCard;
    
    // Menu
    private JMenuBar menuBar;
    
    public MenuPrincipal(Integer usuarioId) {
        this.usuarioId = usuarioId;
        carregarPerfilUsuario();
        initComponents();
        // setupMenu(); // Removido: usando painel de navegação ao invés de menu bar
        setupCards();
        setupKeyboardShortcuts();
    }
    
    private void carregarPerfilUsuario() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
            if (usuario != null) {
                this.perfilUsuario = usuario.getPerfil();
            } else {
                this.perfilUsuario = "TESOUREIRO";
            }
        } catch (SQLException e) {
            this.perfilUsuario = "TESOUREIRO";
        }
    }
    
    private boolean temAcessoAdminSystem() {
        // Apenas SuperAdmin tem acesso
        return "SUPERADMIN".equalsIgnoreCase(perfilUsuario);
    }
    
    private boolean temAcessoLogs() {
        // Apenas SuperAdmin tem acesso
        return "SUPERADMIN".equalsIgnoreCase(perfilUsuario);
    }
    
    private void initComponents() {
        setTitle("Menu Principal - Gestão Financeira Ministerial");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 700));
        
        // Configurar layout principal
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new java.awt.Color(240, 240, 240));
        
        // Criar painel de cards com estilo
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new java.awt.Color(240, 240, 240));
        
        // Criar as telas (cards) com o usuário ID
        dashboardCard = new DashboardCard(usuarioId, this);
        contasCard = new ContasCard(usuarioId, this);
        categoriasCard = new CategoriasCard(usuarioId, this);
        transacoesCard = new TransacoesCard(usuarioId, this);
        relatoriosCard = new RelatoriosCard(usuarioId, this);
        admiSystemCard = new AdmiSystemCard(usuarioId, this);
        logsCard = new LogsCard(usuarioId, this);
        configuracoesCard = new view.telas.componentes.ConfiguracoesCard(usuarioId, this);
        
        // Adicionar cards ao painel
        cardPanel.add(dashboardCard, "INICIO");
        cardPanel.add(contasCard, "CONTAS");
        cardPanel.add(categoriasCard, "CATEGORIAS");
        cardPanel.add(transacoesCard, "TRANSAÇÕES");
        cardPanel.add(relatoriosCard, "RELATORIOS");
        cardPanel.add(admiSystemCard, "ADMISYSTEM");
        cardPanel.add(logsCard, "LOGS");
        cardPanel.add(configuracoesCard, "CONFIGURACOES");
        
        // Painel de navegação superior
        JPanel panelNavegacao = criarPainelNavegacao();
        
        // Adicionar ao frame
        add(panelNavegacao, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        
        // Centralizar na tela
        pack();
        setLocationRelativeTo(null);
        
        // Mostrar tela inicial
        mostrarTela("INICIO");
    }
    
    private JPanel criarPainelNavegacao() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new java.awt.Color(220, 220, 220));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(150, 150, 150)));
        
        // Botões de navegação
        JButton btnInicio = new JButton("Página Inicial");
        JButton btnContas = new JButton("Contas");
        JButton btnCategorias = new JButton("Categorias");
        JButton btnTransacoes = new JButton("Transações");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnConfiguracoes = new JButton("Configurações");
        JButton btnAdmiSystem = new JButton("AdmiSystem");
        JButton btnLogs = new JButton("Gestão de Logs");
        JButton btnVoltar = new JButton("Voltar ao Dashboard");
        
        // Estilo dos botões usando UIStyler
        for (JButton btn : new JButton[]{btnInicio, btnContas, btnCategorias, btnTransacoes, btnRelatorios, btnConfiguracoes}) {
            UIStyler.styleSecondaryButton(btn);
        }
        UIStyler.styleDangerButton(btnAdmiSystem); // Destacar como acesso crítico
        UIStyler.styleWarningButton(btnLogs); // Destacar como acesso crítico
        UIStyler.styleNeutralButton(btnVoltar);
        
        panel.add(btnInicio);
        panel.add(btnContas);
        panel.add(btnCategorias);
        panel.add(btnTransacoes);
        panel.add(btnRelatorios);
        panel.add(btnConfiguracoes);
        
        // SuperAdmin tem acesso a TUDO - AdminSystem e Logs
        if (temAcessoAdminSystem()) {
            panel.add(btnAdmiSystem);
        }
        
        if (temAcessoLogs()) {
            panel.add(btnLogs);
        }
        
        panel.add(Box.createHorizontalGlue());
        panel.add(btnVoltar);
        
        // Ações dos botões
        btnInicio.addActionListener(e -> mostrarTela("INICIO"));
        btnContas.addActionListener(e -> mostrarTela("CONTAS"));
        btnCategorias.addActionListener(e -> mostrarTela("CATEGORIAS"));
        btnTransacoes.addActionListener(e -> mostrarTela("TRANSAÇÕES"));
        btnRelatorios.addActionListener(e -> mostrarTela("RELATORIOS"));
        btnConfiguracoes.addActionListener(e -> mostrarTela("CONFIGURACOES"));
        
        if (temAcessoAdminSystem()) {
            btnAdmiSystem.addActionListener(e -> mostrarTela("ADMISYSTEM"));
        }
        
        if (temAcessoLogs()) {
            btnLogs.addActionListener(e -> mostrarTela("LOGS"));
        }
        
        btnVoltar.addActionListener(e -> dispose());
        
        return panel;
    }
    
    private void setupMenu() {
        menuBar = new JMenuBar();
        
        // Menu Início
        JMenu menuInicio = new JMenu("Início");
        menuInicio.setMnemonic(KeyEvent.VK_I);
        
        JMenuItem itemInicio = new JMenuItem("Página Inicial");
        itemInicio.addActionListener(e -> mostrarTela("INICIO"));
        menuInicio.add(itemInicio);
        
        // Menu Contas
        JMenu menuContas = new JMenu("Contas");
        menuContas.setMnemonic(KeyEvent.VK_C);
        
        JMenuItem itemContas = new JMenuItem("Gerenciar Contas");
        itemContas.addActionListener(e -> mostrarTela("CONTAS"));
        menuContas.add(itemContas);
        
        // Menu Categorias
        JMenu menuCategorias = new JMenu("Categorias");
        menuCategorias.setMnemonic(KeyEvent.VK_G);
        
        JMenuItem itemCategorias = new JMenuItem("Gerenciar Categorias");
        itemCategorias.addActionListener(e -> mostrarTela("CATEGORIAS"));
        menuCategorias.add(itemCategorias);
        
        // Menu Transações
        JMenu menuTransacoes = new JMenu("Transações");
        menuTransacoes.setMnemonic(KeyEvent.VK_T);
        
        JMenuItem itemTransacoes = new JMenuItem("Gerenciar Transações");
        itemTransacoes.addActionListener(e -> mostrarTela("TRANSAÇÕES"));
        menuTransacoes.add(itemTransacoes);
        
        // Menu Relatórios
        JMenu menuRelatorios = new JMenu("Relatórios");
        menuRelatorios.setMnemonic(KeyEvent.VK_R);
        
        JMenuItem itemRelatorios = new JMenuItem("Relatórios");
        itemRelatorios.addActionListener(e -> mostrarTela("RELATORIOS"));
        menuRelatorios.add(itemRelatorios);
        
        // Adicionar todos os menus à barra
        menuBar.add(menuInicio);
        menuBar.add(menuContas);
        menuBar.add(menuCategorias);
        menuBar.add(menuTransacoes);
        menuBar.add(menuRelatorios);
        
        // Menu Ajuda (à direita)
        menuBar.add(Box.createHorizontalGlue()); // Empurra para a direita
        
        JMenu menuAjuda = new JMenu("Ajuda");
        menuAjuda.setMnemonic(KeyEvent.VK_A);
        
        JMenuItem itemSobre = new JMenuItem("Sobre");
        JMenuItem itemSair = new JMenuItem("Sair");
        
        itemSobre.addActionListener(e -> mostrarSobre());
        itemSair.addActionListener(e -> sair());
        
        menuAjuda.add(itemSobre);
        menuAjuda.addSeparator();
        menuAjuda.add(itemSair);
        
        menuBar.add(menuAjuda);
        
        setJMenuBar(menuBar);
    }
    
    private void setupCards() {
        // Configurar callbacks para atualização
        if (contasCard != null) {
            contasCard.setOnUpdateCallback(() -> {
                if (dashboardCard != null) {
                    dashboardCard.atualizarDados();
                }
            });
        }
        
        if (categoriasCard != null) {
            categoriasCard.setOnUpdateCallback(() -> {
                if (transacoesCard != null) {
                    transacoesCard.recarregarCategorias();
                }
            });
        }
        
        if (transacoesCard != null) {
            transacoesCard.setOnUpdateCallback(() -> {
                if (dashboardCard != null) {
                    dashboardCard.atualizarDados();
                }
            });
        }
    }
    
    private void setupKeyboardShortcuts() {
        // Atalhos de teclado
        InputMap inputMap = cardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = cardPanel.getActionMap();
        
        // Ctrl+I: Início
        inputMap.put(KeyStroke.getKeyStroke("control I"), "showInicio");
        actionMap.put("showInicio", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("INICIO");
            }
        });
        
        // Ctrl+C: Contas
        inputMap.put(KeyStroke.getKeyStroke("control C"), "showContas");
        actionMap.put("showContas", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("CONTAS");
            }
        });
        
        // Ctrl+G: Categorias
        inputMap.put(KeyStroke.getKeyStroke("control G"), "showCategorias");
        actionMap.put("showCategorias", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("CATEGORIAS");
            }
        });
        
        // Ctrl+T: Transações
        inputMap.put(KeyStroke.getKeyStroke("control T"), "showTransacoes");
        actionMap.put("showTransacoes", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("TRANSAÇÕES");
            }
        });
        
        // Ctrl+R: Relatórios
        inputMap.put(KeyStroke.getKeyStroke("control R"), "showRelatorios");
        actionMap.put("showRelatorios", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mostrarTela("RELATORIOS");
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
            case "INICIO":
                if (dashboardCard != null) {
                    dashboardCard.atualizarDados();
                }
                break;
            case "CONTAS":
                if (contasCard != null) {
                    contasCard.carregarDados();
                }
                break;
            case "CATEGORIAS":
                if (categoriasCard != null) {
                    categoriasCard.carregarDados();
                }
                break;
            case "TRANSAÇÕES":
                if (transacoesCard != null) {
                    transacoesCard.carregarDados();
                }
                break;
            case "RELATORIOS":
                if (relatoriosCard != null) {
                    relatoriosCard.carregarDados();
                }
                break;
        }
    }
    
    private void atualizarTitulo(String tela) {
        String titulo = "Sistema de Gestão Financeira - ";
        switch (tela) {
            case "INICIO":
                titulo += "Página Inicial";
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
        if (contasCard != null) {
            contasCard.mostrarFormularioNovaConta();
        }
    }
    
    public void mostrarFormularioNovaTransacao() {
        mostrarTela("TRANSAÇÕES");
        if (transacoesCard != null) {
            transacoesCard.mostrarAbaCadastro();
        }
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
            dispose(); // Apenas fecha a janela, não o sistema
        }
    }
}