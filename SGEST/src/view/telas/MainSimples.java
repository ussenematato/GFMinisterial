package view.telas;

import javax.swing.*;

public class MainSimples {
    public static void main(String[] args) {
        // Usar o Look and Feel do sistema (sem FlatLaf)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Iniciando DashboardView...");
                DashboardView dashboard = new DashboardView(1);
                dashboard.setVisible(true);
                System.out.println("Aplicação iniciada com sucesso!");
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
