package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    
    // Metical - Moeda de Moçambique
    private static final Locale MOZAMBIQUE = new Locale("pt", "MZ");
    private static final DecimalFormat CURRENCY_FORMAT;
    private static final DecimalFormat NUMBER_FORMAT;
    
    // Símbolo do Metical
    private static final String METICAL_SYMBOL = "MT";
    
    static {
        // Usar locale pt_BR para formatação numérica (separadores), mas com símbolo customizado
        Locale br = new Locale("pt", "BR");
        CURRENCY_FORMAT = (DecimalFormat) NumberFormat.getNumberInstance(br);
        CURRENCY_FORMAT.setMinimumFractionDigits(2);
        CURRENCY_FORMAT.setMaximumFractionDigits(2);
        
        NUMBER_FORMAT = (DecimalFormat) NumberFormat.getNumberInstance(br);
        NUMBER_FORMAT.setMinimumFractionDigits(2);
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }
    
    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return METICAL_SYMBOL + " " + CURRENCY_FORMAT.format(value);
    }
    
    public static String formatNumber(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return NUMBER_FORMAT.format(value);
    }
    
    public static BigDecimal parseCurrency(String currencyString) {
        try {
            String cleanString = currencyString.replaceAll("[MT\\$\\.]", "")
                .replace(",", ".")
                .replace(" ", "")
                .trim();
            return new BigDecimal(cleanString);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    public static String formatToHumanReadable(BigDecimal value) {
        if (value == null) {
            return "MT 0,00";
        }
        
        double val = value.doubleValue();
        if (val >= 1_000_000) {
            return String.format("MT %.1fM", val / 1_000_000);
        } else if (val >= 1_000) {
            return String.format("MT %.1fK", val / 1_000);
        } else {
            return formatCurrency(value);
        }
    }
}