package org.example.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;

/**
 * 참고로 Temporal 을 구현하는 모든 시간관련 객체는 불변이다.
 */
public class LocalDateEx {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2017, 9, 21); // 2017-09-21
        int year = date.getYear(); // 2017
        Month month = date.getMonth(); // SEPTEMBER
        int dayOfMonth = date.getDayOfMonth(); // 21
        DayOfWeek dayOfWeek = date.getDayOfWeek(); // THURSDAY
        int len = date.lengthOfMonth(); // 31 (3월의 일 수)
        boolean leapYear = date.isLeapYear(); // false (윤년이 아님)

        LocalDate now = LocalDate.now(); // 현재 날짜

        int year2 = date.get(ChronoField.YEAR); // 2017
        int month2 = date.get(ChronoField.MONTH_OF_YEAR); // 9
        int day = date.get(ChronoField.DAY_OF_MONTH); // 21
        System.out.println(year2 + " " + month2 + " " + day);

        LocalDate parseDate = LocalDate.parse("2017-09-21");

        LocalDate date2 = LocalDate.of(2014, 3, 18); // 2014-03-18
        LocalDate date3 = date2.with(ChronoField.MONTH_OF_YEAR, 7); // 2014-07-18
        System.out.println("date3 = " + date3);
        LocalDate date4 = date3.plusYears(7).minusDays(8); // 2021-07-10
        System.out.println("date4 = " + date4);

    }
}
