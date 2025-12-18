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
                
                // Verificar se é despesa ou receita (baseado na coluna tipo)
                try {
                    int tipoColumn = getTipoColumn(table);
                    if (tipoColumn >= 0) {
                        String tipo = (String) table.getValueAt(row, tipoColumn);
                        if ("DESPESA".equals(tipo)) {
                            setForeground(new Color(229, 57, 53));
                        } else if ("RECEITA".equals(tipo)) {
                            setForeground(new Color(46, 125, 50));
                        }
                    }
                } catch (Exception e) {
                    // Se não conseguir determinar o tipo, usa cor padrão
                }
                
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
        
        private int getTipoColumn(JTable table) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                String columnName = table.getColumnName(i);
                if (columnName != null && 
                    (columnName.equalsIgnoreCase("tipo") || 
                     columnName.equalsIgnoreCase("status") ||
                     columnName.equalsIgnoreCase("categoria"))) {
                    return i;
                }
            }
            return -1;
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
                
                if ("DESPESA".equalsIgnoreCase(status) || 
                    "PENDENTE".equalsIgnoreCase(status) ||
                    "NÃO PAGO".equalsIgnoreCase(status)) {
                    label.setForeground(new Color(229, 57, 53));
                    label.setText(status);
                } else if ("RECEITA".equalsIgnoreCase(status) || 
                           "PAGO".equalsIgnoreCase(status)) {
                    label.setForeground(new Color(46, 125, 50));
                    label.setText(status);
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