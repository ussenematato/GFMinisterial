package view.telas;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class MainSimples {

    public static void main(String[] args) {
        // Configurar Look and Feel (FlatLaf)
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Iniciar a aplicação com DashboardView
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DashboardView(1).setVisible(true);
            }
        });
    }
}
