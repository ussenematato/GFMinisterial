package view.telas.utils;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validators {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static boolean validarCampoObrigatorio(JTextField campo, String nomeCampo) {
        if (campo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(campo, 
                String.format("O campo '%s' é obrigatório.", nomeCampo),
                "Validação", 
                JOptionPane.WARNING_MESSAGE);
            campo.requestFocus();
            return false;
        }
        return true;
    }
    
    public static boolean validarValor(JTextField campo, String nomeCampo) {
        try {
            BigDecimal valor = new BigDecimal(campo.getText().trim());
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(campo,
                    String.format("O campo '%s' deve ser maior que zero.", nomeCampo),
                    "Validação",
                    JOptionPane.WARNING_MESSAGE);
                campo.requestFocus();
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(campo,
                String.format("O campo '%s' contém um valor inválido.", nomeCampo),
                "Validação",
                JOptionPane.WARNING_MESSAGE);
            campo.requestFocus();
            return false;
        }
    }
    
    public static boolean validarData(JTextField campo, String nomeCampo) {
        try {
            LocalDate.parse(campo.getText().trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(campo,
                String.format("O campo '%s' deve conter uma data válida no formato dd/MM/aaaa.", nomeCampo),
                "Validação",
                JOptionPane.WARNING_MESSAGE);
            campo.requestFocus();
            return false;
        }
    }
    
    public static LocalDate parseData(String dataString) {
        try {
            return LocalDate.parse(dataString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static String formatData(LocalDate data) {
        return data != null ? data.format(DATE_FORMATTER) : "";
    }
    
    public static BigDecimal parseValor(String valorString) {
        try {
            String cleanString = valorString.replaceAll("[R\\$\\.]", "")
                .replace(",", ".")
                .replace(" ", "")
                .trim();
            return new BigDecimal(cleanString);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}