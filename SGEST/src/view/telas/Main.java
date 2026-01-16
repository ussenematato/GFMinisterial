package view.telas;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel (FlatLaf)
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