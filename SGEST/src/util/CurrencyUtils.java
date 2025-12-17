package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    
    private static final Locale BRAZIL = new Locale("pt", "BR");
    private static final DecimalFormat CURRENCY_FORMAT = 
        (DecimalFormat) NumberFormat.getCurrencyInstance(BRAZIL);
    private static final DecimalFormat NUMBER_FORMAT = 
        (DecimalFormat) NumberFormat.getNumberInstance(BRAZIL);
    
    static {
        NUMBER_FORMAT.setMinimumFractionDigits(2);
        NUMBER_FORMAT.setMaximumFractionDigits(2);
    }
    
    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return CURRENCY_FORMAT.format(value);
    }
    
    public static String formatNumber(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return NUMBER_FORMAT.format(value);
    }
    
    public static BigDecimal parseCurrency(String currencyString) {
        try {
            String cleanString = currencyString.replaceAll("[R\\$\\.]", "")
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
            return "R$ 0,00";
        }
        
        double val = value.doubleValue();
        if (val >= 1_000_000) {
            return String.format("R$ %.1fM", val / 1_000_000);
        } else if (val >= 1_000) {
            return String.format("R$ %.1fK", val / 1_000);
        } else {
            return formatCurrency(value);
        }
    }
}