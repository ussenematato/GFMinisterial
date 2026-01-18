package view.telas;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel - usar padrão do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Usar L&F padrão em caso de erro
            e.printStackTrace();
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