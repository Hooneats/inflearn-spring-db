package org.example.date;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class TransParsingEx {

    public static void main(String[] args) {
        LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalDate date2 = date1.withYear(2022); // 2022-09-21
        LocalDate date3 = date2.withDayOfMonth(23); // 2022-09-23
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 3); // 2022-03-23

        LocalDate date5 = date4.plusWeeks(1); // 2022-03-30
        LocalDate date6 = date5.minusYears(1); // 2021-03-30
        LocalDate date7 = date6.plus(5, ChronoUnit.MONTHS); // 2021-08-30
        System.out.println("date7 = " + date7);
    }

    /**
     * TODO : LocalDate, LocalDateTime, LocalTime, Instant 등 날짜와 시간을 표현하는 클래스의 공통 메서드
     *
     * TODO : static (정적) 메서드 :
     *  from
     *  now
     *  of
     *  parse
     *
     * TODO : instance (인스턴스) 메서드 :
     *  atOffset
     *  atZone
     *  format
     *  get
     *  minus
     *  plus
     *  with
     */
}
