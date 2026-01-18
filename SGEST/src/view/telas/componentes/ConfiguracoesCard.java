package view.telas.componentes;

import view.telas.MenuPrincipal;
import view.telas.UserProfileView;
import javax.swing.*;
import java.awt.*;

public class ConfiguracoesCard extends CardBase {
    
    public ConfiguracoesCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel central com opções
        JPanel panelOpcoes = criarPainelOpcoes();
        add(panelOpcoes, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelOpcoes() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Botão: Meu Perfil
        JButton btnMeuPerfil = criarBotaoConfiguracao(
            "Meu Perfil",
            "Edite seus dados pessoais e senha",
            new Color(30, 136, 229)
        );
        btnMeuPerfil.addActionListener(e -> abrirMeuPerfil());
        
        // Botão: Preferências
        JButton btnPreferencias = criarBotaoConfiguracao(
            "Preferências do Sistema",
            "Altere as preferências de exibição",
            new Color(156, 39, 176)
        );
        btnPreferencias.addActionListener(e -> mostrarMensagemSucesso("Funcionalidade em desenvolvimento"));
        
        // Botão: Sobre
        JButton btnSobre = criarBotaoConfiguracao(
            "Sobre o Sistema",
            "Informações do SGEST",
            new Color(76, 175, 80)
        );
        btnSobre.addActionListener(e -> mostrarSobre());
        
        panel.add(btnMeuPerfil);
        panel.add(btnPreferencias);
        panel.add(btnSobre);
        
        return panel;
    }
    
    private JButton criarBotaoConfiguracao(String titulo, String descricao, Color cor) {
        JButton botao = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(cor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        
        botao.setLayout(new BorderLayout());
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setOpaque(false);
        
        JPanel panelTexto = new JPanel(new BorderLayout());
        panelTexto.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblDescricao = new JLabel(descricao);
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescricao.setForeground(new Color(220, 220, 220));
        
        JPanel panelConteudo = new JPanel(new GridLayout(2, 1, 0, 5));
        panelConteudo.setOpaque(false);
        panelConteudo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panelConteudo.add(lblTitulo);
        panelConteudo.add(lblDescricao);
        
        botao.add(panelConteudo, BorderLayout.CENTER);
        botao.setPreferredSize(new Dimension(400, 80));
        
        return botao;
    }
    
    private void abrirMeuPerfil() {
        UserProfileView dialog = new UserProfileView(usuarioId, 
            (JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }
    
    private void mostrarSobre() {
        String msg = "Sistema de Gestão Financeira Ministerial (SGEST)\n\n" +
                     "Versão: 1.0\n" +
                     "Desenvolvido para gerenciar contas, transações e relatórios financeiros.\n\n" +
                     "© 2026 - Todos os direitos reservados.";
        JOptionPane.showMessageDialog(this, msg, "Sobre o SGEST", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void carregarDados() {
        // Não há dados para carregar nesta tela
    }
}
