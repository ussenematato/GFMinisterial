package view.telas.componentes;

import view.telas.LogsView;
import view.telas.MenuPrincipal;
import util.UIStyler;
import javax.swing.*;
import java.awt.*;

public class LogsCard extends CardBase {
    
    public LogsCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        initComponents();
    }
    
    @Override
    public void carregarDados() {
        // Não há dados a carregar neste card
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));
        
        // Painel central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(240, 240, 240));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Ícone ou título
        JLabel lblTitulo = new JLabel("Sistema de Gestão de Logs");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        panelCentral.add(lblTitulo, gbc);
        
        gbc.gridy++;
        JLabel lblDescricao = new JLabel("Visualize e gerencie todos os registros de auditoria do sistema");
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescricao.setForeground(new Color(100, 100, 100));
        panelCentral.add(lblDescricao, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(30, 10, 10, 10);
        
        // Botão para abrir a tela de logs
        JButton btnAbrirLogs = new JButton("Abrir Gestão de Logs");
        btnAbrirLogs.setFont(new Font("Arial", Font.BOLD, 14));
        btnAbrirLogs.setPreferredSize(new Dimension(300, 50));
        UIStyler.styleSuccessButton(btnAbrirLogs);
        
        btnAbrirLogs.addActionListener(e -> abrirTelaLogs());
        
        panelCentral.add(btnAbrirLogs, gbc);
        
        // Adicionar painel central ao frame
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private void abrirTelaLogs() {
        JFrame topLevel = (JFrame) SwingUtilities.getWindowAncestor(this);
        LogsView logsView = new LogsView(topLevel, usuarioId);
        logsView.setVisible(true);
    }
}
