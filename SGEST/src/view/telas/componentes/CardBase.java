package view.telas.componentes;

import javax.swing.*;
import java.awt.*;
import view.telas.MenuPrincipal;
import util.UIStyler;

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
        
        // Mapear cores ao estilo UIStyler
        if (corFundo.getRGB() == new Color(76, 175, 80).getRGB() || 
            corFundo.getRGB() == new Color(60, 179, 113).getRGB() ||
            corFundo.getRGB() == new Color(34, 139, 34).getRGB()) {
            UIStyler.styleSuccessButton(botao);
        } else if (corFundo.getRGB() == new Color(244, 67, 54).getRGB() || 
                   corFundo.getRGB() == new Color(220, 20, 60).getRGB()) {
            UIStyler.styleDangerButton(botao);
        } else if (corFundo.getRGB() == new Color(255, 193, 7).getRGB() || 
                   corFundo.getRGB() == new Color(255, 152, 0).getRGB()) {
            UIStyler.styleWarningButton(botao);
        } else if (corFundo.getRGB() == new Color(158, 158, 158).getRGB() || 
                   corFundo.getRGB() == new Color(169, 169, 169).getRGB()) {
            UIStyler.styleNeutralButton(botao);
        } else {
            // Padrão: Secondary
            UIStyler.styleSecondaryButton(botao);
        }
        
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