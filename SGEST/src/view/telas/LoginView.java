package view.telas;

import controller.UsuarioController;
import controller.LogController;
import model.entity.Usuario;
import util.UIStyler;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class LoginView extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnRecuperarSenha;
    private JButton btnSair;
    private JLabel lblMensagem;
    private UsuarioController usuarioController;
    private Usuario usuarioLogado;
    private LogController logController;
    
    public LoginView() {
        this.usuarioController = new UsuarioController();
        this.usuarioLogado = null;
        this.logController = new LogController();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("SGEST - Sistema de Gestão Financeira Ministerial");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(false);
        setResizable(false);
        
        // Criar painel principal com dois lados
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        
        // Painel esquerdo com logo
        JPanel panelEsquerdo = criarPanelLogo();
        
        // Painel direito com formulário de login
        JPanel panelDireito = criarPanelLogin();
        
        mainPanel.add(panelEsquerdo);
        mainPanel.add(panelDireito);
        
        add(mainPanel);
        
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    
    private JPanel criarPanelLogo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 255)); // Branco como fundo
        
        // Tentar carregar logo
        try {
            // Tentar carregar LogoOficial.jpeg primeiro
            File logoFile = new File("src/view/telas/logos/Logo.jpeg");
            if (!logoFile.exists()) {
                logoFile = new File("src/view/telas/logos/Logo.jpeg");
            }
            
            if (logoFile.exists()) {
                BufferedImage img = ImageIO.read(logoFile);
                
                // Redimensionar a imagem para caber no painel
                Image scaledImg = img.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImg);
                
                JLabel lblLogo = new JLabel(icon);
                lblLogo.setHorizontalAlignment(JLabel.CENTER);
                lblLogo.setVerticalAlignment(JLabel.CENTER);
                
                panel.add(lblLogo, BorderLayout.CENTER);
            } else {
                // Se não encontrar logo, mostrar texto
                JLabel lblLogo = new JLabel("SGEST");
                lblLogo.setFont(new Font("Arial", Font.BOLD, 48));
                lblLogo.setForeground(Color.WHITE);
                lblLogo.setHorizontalAlignment(JLabel.CENTER);
                lblLogo.setVerticalAlignment(JLabel.CENTER);
                
                JLabel lblSubtitulo = new JLabel("Sistema de Gestão Financeira Ministerial");
                lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
                lblSubtitulo.setForeground(new Color(200, 200, 200));
                lblSubtitulo.setHorizontalAlignment(JLabel.CENTER);
                
                JPanel panelTexto = new JPanel(new BorderLayout(0, 20));
                panelTexto.setBackground(new Color(25, 25, 112));
                panelTexto.add(lblLogo, BorderLayout.CENTER);
                panelTexto.add(lblSubtitulo, BorderLayout.SOUTH);
                
                panel.add(panelTexto, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            // Fallback: mostrar texto
            JLabel lblLogo = new JLabel("SGEST");
            lblLogo.setFont(new Font("Arial", Font.BOLD, 48));
            lblLogo.setForeground(Color.WHITE);
            lblLogo.setHorizontalAlignment(JLabel.CENTER);
            lblLogo.setVerticalAlignment(JLabel.CENTER);
            
            panel.add(lblLogo, BorderLayout.CENTER);
            e.printStackTrace();
        }
        
        return panel;
    }
    
    private JPanel criarPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(235, 108, 67));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 30, 15, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 30, 30, 30);
        
        JLabel lblTitulo = new JLabel("Bem-vindo ao SGEST");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(25, 25, 112));
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblTitulo, gbc);
        
        // Email
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 30, 5, 30);
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblEmail, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 30, 15, 30);
        
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(200, 35));
        panel.add(txtEmail, gbc);
        
        // Senha
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 30, 5, 30);
        
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblSenha, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 30, 15, 30);
        
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSenha.setPreferredSize(new Dimension(200, 35));
        panel.add(txtSenha, gbc);
        
        // Mensagem de erro
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 30, 10, 30);
        
        lblMensagem = new JLabel("");
        lblMensagem.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMensagem.setForeground(new Color(200, 0, 0));
        lblMensagem.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblMensagem, gbc);
        
        // Botões
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 30, 10, 15);
        
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEntrar.setPreferredSize(new Dimension(100, 40));
        UIStyler.styleSuccessButton(btnEntrar);
        btnEntrar.addActionListener(e -> autenticar());
        panel.add(btnEntrar, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 15, 10, 30);
        
        btnRecuperarSenha = new JButton("Recuperar Senha");
        btnRecuperarSenha.setFont(new Font("Arial", Font.BOLD, 12));
        btnRecuperarSenha.setPreferredSize(new Dimension(140, 40));
        UIStyler.styleSecondaryButton(btnRecuperarSenha);
        btnRecuperarSenha.addActionListener(e -> recuperarSenha());
        panel.add(btnRecuperarSenha, gbc);
        
        // Botão Sair
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 30, 20, 30);
        
        btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.setPreferredSize(new Dimension(200, 40));
        UIStyler.styleDangerButton(btnSair);
        btnSair.addActionListener(e -> sair());
        panel.add(btnSair, gbc);

                // Remover aparência de botão (sem borda/preenchimento) para o card contas
        for (JButton btn : new JButton[]{btnEntrar, btnRecuperarSenha, btnSair}) {
            btn.setBorderPainted(false);
        }
        
        return panel;
    }
    
    private void autenticar() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        
        // Validações
        if (email.isEmpty()) {
            lblMensagem.setText("Email é obrigatório");
            return;
        }
        
        if (senha.isEmpty()) {
            lblMensagem.setText("Senha é obrigatória");
            return;
        }
        
        try {
            // Buscar usuário por email
            model.dao.UsuarioDAO usuarioDAO = new model.dao.UsuarioDAO();
            Usuario usuario = usuarioDAO.buscarPorEmail(email);
            
            if (usuario == null) {
                lblMensagem.setText("Email ou senha inválidos");
                // Registrar tentativa de login falhada (email não encontrado)
                try {
                    logController.registrarOperacao(null, email, "LOGIN", "Tentativa de login falhada: email não encontrado", "usuarios", null, "FALHA");
                } catch (Exception ex) {
                    // não propagar erro de log
                }
                txtSenha.setText("");
                return;
            }
            
            // Verificar se usuário está ativo
            if (!usuario.getAtivo()) {
                lblMensagem.setText("Usuário inativo. Contacte o administrador.");
                try {
                    logController.registrarOperacao(usuario.getId(), usuario.getNome(), "LOGIN", "Tentativa de login: usuário inativo", "usuarios", usuario.getId(), "FALHA");
                } catch (Exception ex) {}
                return;
            }
            
            // Verificar senha
            if (!UsuarioController.verificarSenha(senha, usuario.getSenhaHash())) {
                lblMensagem.setText("Email ou senha inválidos");
                // Registrar tentativa de login falhada (senha inválida)
                try {
                    logController.registrarOperacao(usuario.getId(), usuario.getNome(), "LOGIN", "Tentativa de login falhada: senha inválida", "usuarios", usuario.getId(), "FALHA");
                } catch (Exception ex) {}
                txtSenha.setText("");
                return;
            }
            
            // Login bem-sucedido
            this.usuarioLogado = usuario;
            lblMensagem.setText("");
            try {
                logController.registrarOperacao(usuario.getId(), usuario.getNome(), "LOGIN", "Login bem-sucedido", "usuarios", usuario.getId(), "SUCESSO");
            } catch (Exception ex) {}
            
            // Abrir MenuPrincipal com todos os dados
            SwingUtilities.invokeLater(() -> {
                MenuPrincipal menuPrincipal = new MenuPrincipal(usuario.getId());
                menuPrincipal.setVisible(true);
                this.dispose();
            });
            
        } catch (Exception e) {
            lblMensagem.setText("Erro ao autenticar: " + e.getMessage());
            try {
                logController.registrarOperacao(null, email, "LOGIN", "Erro ao autenticar: " + e.getMessage(), "usuarios", null, "FALHA");
            } catch (Exception ex) {}
            e.printStackTrace();
        }
    }
    
    private void recuperarSenha() {
        String email = JOptionPane.showInputDialog(this, 
            "Digite seu email para recuperar a senha:", 
            "Recuperação de Senha", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (email != null && !email.trim().isEmpty()) {
            try {
                model.dao.UsuarioDAO usuarioDAO = new model.dao.UsuarioDAO();
                Usuario usuario = usuarioDAO.buscarPorEmail(email.trim());
                
                if (usuario == null) {
                    JOptionPane.showMessageDialog(this,
                        "Email não encontrado no sistema.",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Gerar senha temporária
                    String novaSenha = gerarSenhaTemporaria();
                    
                    // Atualizar no banco
                    usuarioDAO.atualizarSenha(usuario.getId(), UsuarioController.hashSenha(novaSenha));
                    
                    JOptionPane.showMessageDialog(this,
                        "Nova senha temporária: " + novaSenha + "\n\n" +
                        "Uma confirmação foi enviada para: " + usuario.getEmail(),
                        "Senha Recuperada",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    lblMensagem.setText("Senha resetada com sucesso!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao recuperar senha: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String gerarSenhaTemporaria() {
        // Gerar senha temporária (8 caracteres)
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder senha = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * caracteres.length());
            senha.append(caracteres.charAt(index));
        }
        
        return senha.toString();
    }
    
    private void sair() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja sair?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Erro ao iniciar sistema: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
