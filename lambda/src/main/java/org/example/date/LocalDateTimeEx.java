package org.example.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class LocalDateTimeEx {

    public static void main(String[] args) {
        LocalDateTime localDateTime = // 2017-09-21T13:45:20
                LocalDateTime.of(217, Month.SEPTEMBER, 21, 13, 45, 20);

        LocalDate date = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
        LocalDateTime localDateTime2 = LocalDateTime.of(date, time);

        LocalDateTime localDateTime3 = date.atTime(13, 45, 20);

        LocalDateTime localDateTime4 = date.atTime(time);

        LocalDateTime localDateTime5 = time.atDate(date);

        LocalDate date2 = localDateTime.toLocalDate(); // 2017-09-21
        LocalTime time2 = localDateTime.toLocalTime(); // 13:45:20
    }
}
