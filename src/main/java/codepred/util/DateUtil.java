package codepred.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static LocalDate convertStringToLocalDate(String dateString){

        // Define the pattern that matches the format of the input string
        String pattern = "yyyy-MM-dd";

        // Create a DateTimeFormatter using the pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Parse the string into a LocalDate object
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        return localDate;
    }

    public static String addLeadingZerosToDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        return date.format(formatter);
    }

}
