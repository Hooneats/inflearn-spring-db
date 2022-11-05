package org.example.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class DateTimeFormatterEx {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2014, 3, 18);// 2014-03-18
        String formattedDate1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);// 20140318
        String formattedDate2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);// 2014-03-18
        System.out.println("date = " + date);
        System.out.println("formattedDate1 = " + formattedDate1);
        System.out.println("formattedDate2 = " + formattedDate2);

        LocalDate parsedDate1 = LocalDate.parse("20140317", DateTimeFormatter.BASIC_ISO_DATE); // 2014-03-17
        System.out.println("parsedDate1 = " + parsedDate1);
        LocalDate parsedDate2 = LocalDate.parse("2014-03-17", DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-17
        System.out.println("parsedDate2 = " + parsedDate2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate date1 = LocalDate.of(2014, 3, 17); // 2014-03-17
        String stringFormattedDate3 = date1.format(formatter); // String 17/03/14
        System.out.println("stringFormattedDate3 = " + stringFormattedDate3);
        LocalDate parsedDate3 = LocalDate.parse(stringFormattedDate3, formatter); // 2014-03-17
        System.out.println("parsedDate3 = " + parsedDate3);

        DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
        LocalDate date2 = LocalDate.of(2014, 3, 18);
        String stringFormattedItalianDate = date2.format(italianFormatter); // 18. marzo 2014
        System.out.println("stringFormattedItalianDate = " + stringFormattedItalianDate);
        LocalDate parsedDate4 = LocalDate.parse(stringFormattedItalianDate, italianFormatter); // 2014-03-18
        System.out.println("parsedDate4 = " + parsedDate4);

        DateTimeFormatter italianFormatterBuilder = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);
        LocalDate date3 = LocalDate.of(2014, 3, 18);
        String stringFormattedItalianDate2 = date3.format(italianFormatterBuilder); // 18. marzo 2014
        System.out.println("stringFormattedItalianDate2 = " + stringFormattedItalianDate2);
        LocalDate parsedDate5 = LocalDate.parse(stringFormattedItalianDate, italianFormatterBuilder); // 2014-03-18
        System.out.println("parsedDate5 = " + parsedDate5);
    }
}
