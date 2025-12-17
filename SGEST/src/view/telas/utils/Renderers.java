package view.telas.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;

public class Renderers {
    
    public static class SaldoCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal saldo = (BigDecimal) value;
                setText(String.format("R$ %,.2f", saldo));
                
                if (saldo.compareTo(BigDecimal.ZERO) >= 0) {
                    setForeground(new Color(46, 125, 50));
                } else {
                    setForeground(new Color(229, 57, 53));
                }
                
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
    
    public static class ValorCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal valor = (BigDecimal) value;
                setText(String.format("R$ %,.2f", valor));
                
                // Verificar se é despesa ou receita
                try {
                    String tipo = (String) table.getValueAt(row, 3); // Ajuste o índice conforme sua tabela
                    if ("DESPESA".equals(tipo)) {
                        setForeground(new Color(229, 57, 53));
                    } else {
                        setForeground(new Color(46, 125, 50));
                    }
                } catch (Exception e) {
                    // Se não conseguir determinar o tipo, usa cor padrão
                }
                
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
    
    public static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof String) {
                String status = (String) value;
                
                if ("DESPESA".equals(status)) {
                    label.setForeground(new Color(229, 57, 53));
                    label.setText("Despesa");
                } else if ("RECEITA".equals(status)) {
                    label.setForeground(new Color(46, 125, 50));
                    label.setText("Receita");
                }
            }
            
            return label;
        }
    }
    
    public static class CorCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof String) {
                String corHex = (String) value;
                try {
                    Color cor;
                    if (corHex.startsWith("#")) {
                        cor = Color.decode(corHex);
                    } else {
                        cor = Color.decode("#" + corHex);
                    }
                    
                    // Criar um círculo colorido
                    JPanel panelCor = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.setColor(cor);
                            g.fillOval(5, 2, 20, 20);
                            g.setColor(Color.BLACK);
                            g.drawOval(5, 2, 20, 20);
                        }
                    };
                    panelCor.setPreferredSize(new Dimension(30, 25));
                    panelCor.setToolTipText(corHex);
                    
                    return panelCor;
                    
                } catch (Exception e) {
                    label.setText(corHex);
                }
            }
            
            return label;
        }
    }
}