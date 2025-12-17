package view.telas.componentes;

import javax.swing.*;
import java.awt.*;
import view.telas.MenuPrincipal;

public abstract class CardBase extends JPanel {
    
    protected Integer usuarioId;
    protected MenuPrincipal menuPrincipal;
    protected UpdateCallback updateCallback;
    
    public CardBase(Integer usuarioId, MenuPrincipal menuPrincipal) {
        this.usuarioId = usuarioId;
        this.menuPrincipal = menuPrincipal;
        setLayout(new BorderLayout());
    }
    
    // Interface para callbacks
    public interface UpdateCallback {
        void onUpdate();
    }
    
    public void setOnUpdateCallback(UpdateCallback callback) {
        this.updateCallback = callback;
    }
    
    // Métodos abstratos que todas as telas devem implementar
    public abstract void carregarDados();
    
    // Métodos utilitários comuns
    protected JPanel criarPanelComTitulo(String titulo, Component componente) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(titulo),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(componente, BorderLayout.CENTER);
        return panel;
    }
    
    protected JButton criarBotao(String texto, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corFundo.darker(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundo);
            }
        });
        
        return botao;
    }
    
    protected JPanel criarPainelBotoes(JButton... botoes) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        for (JButton botao : botoes) {
            panel.add(botao);
        }
        
        return panel;
    }
    
    protected void mostrarMensagemSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected void mostrarMensagemErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    protected boolean confirmarAcao(String mensagem) {
        int resposta = JOptionPane.showConfirmDialog(this, 
            mensagem, "Confirmação", JOptionPane.YES_NO_OPTION);
        return resposta == JOptionPane.YES_OPTION;
    }
}