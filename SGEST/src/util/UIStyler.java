package util;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitária para estilização profissional de componentes UI
 */
public class UIStyler {
    
    // Cores principais do tema
    public static final Color PRIMARY_COLOR = new Color(25, 25, 112);      // Azul escuro
    public static final Color PRIMARY_LIGHT = new Color(70, 130, 180);     // Azul aço
    public static final Color SUCCESS_COLOR = new Color(60, 179, 113);     // Verde
    public static final Color DANGER_COLOR = new Color(220, 20, 60);       // Vermelho
    public static final Color WARNING_COLOR = new Color(255, 152, 0);      // Laranja
    public static final Color SECONDARY_COLOR = new Color(169, 169, 169);  // Cinza
    public static final Color BG_COLOR = new Color(240, 240, 240);         // Fundo claro
    public static final Color BORDER_COLOR = new Color(150, 150, 150);     // Borda cinza
    
    // Fontes
    private static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 12);
    private static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 16);
    private static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 12);
    
    /**
     * Estiliza um botão padrão (primário)
     */
    public static void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(230, 240, 250));
        button.setForeground(PRIMARY_COLOR);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Estiliza um botão secundário
     */
    public static void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(230, 245, 255));
        button.setForeground(PRIMARY_LIGHT);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(PRIMARY_LIGHT, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Estiliza um botão de sucesso (verde)
     */
    public static void styleSuccessButton(JButton button) {
        button.setBackground(new Color(240, 250, 240));
        button.setForeground(SUCCESS_COLOR);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(SUCCESS_COLOR, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Estiliza um botão de perigo (vermelho)
     */
    public static void styleDangerButton(JButton button) {
        button.setBackground(new Color(255, 240, 240));
        button.setForeground(DANGER_COLOR);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(DANGER_COLOR, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Estiliza um botão de aviso (laranja)
     */
    public static void styleWarningButton(JButton button) {
        button.setBackground(new Color(255, 250, 230));
        button.setForeground(WARNING_COLOR);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(WARNING_COLOR, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Estiliza um botão neutro (cinza)
     */
    public static void styleNeutralButton(JButton button) {
        button.setBackground(new Color(245, 245, 245));
        button.setForeground(SECONDARY_COLOR);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Estiliza um painel com borda e cor de fundo
     */
    public static void stylePanelWithBorder(JPanel panel) {
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));
    }
    
    /**
     * Estiliza um label como título
     */
    public static void styleTitleLabel(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(PRIMARY_COLOR);
    }
    
    /**
     * Estiliza um label normal
     */
    public static void styleLabel(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(Color.BLACK);
    }
}
