package codepred.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private DateUtil() {
    }

    public static LocalDate convertStringToLocalDate(final String dateString){
        final var pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateString, formatter);
    }

    public static String addLeadingZerosToDate(final LocalDate date) {
        final var pattern = "dd/MM";
        final var formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

}
