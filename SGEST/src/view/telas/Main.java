package view.telas;

import view.telas.MenuPrincipal;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel (FlatLaf)
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
            // Ou para tema escuro: com.formdev.flatlaf.FlatDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            // Para teste, use usuário ID 1
            MenuPrincipal menu = new MenuPrincipal(1);
            menu.setVisible(true);
            menu.setLocationRelativeTo(null);
        });
    }
}