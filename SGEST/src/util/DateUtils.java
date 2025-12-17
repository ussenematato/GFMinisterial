package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DISPLAY_FORMATTER = 
        DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
    
    public static LocalDate parse(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    public static String format(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }
    
    public static String formatDisplay(LocalDate date) {
        return date != null ? date.format(DISPLAY_FORMATTER) : "";
    }
    
    public static boolean isValid(String dateString) {
        return parse(dateString) != null;
    }
    
    public static LocalDate getFirstDayOfMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }
    
    public static LocalDate getLastDayOfMonth() {
        return LocalDate.now().withDayOfMonth(
            LocalDate.now().lengthOfMonth()
        );
    }
}