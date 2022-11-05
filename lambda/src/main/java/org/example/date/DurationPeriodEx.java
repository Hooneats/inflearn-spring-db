package org.example.date;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class DurationPeriodEx {

    public static void main(String[] args) {
        // Duration 은 초와 나노초로 시간 단위를 표현하므로 시간이 들어가야한다.
        LocalDateTime dateTime1 = LocalDateTime.of(2022, 10, 21, 10,21,0);
        LocalDateTime dateTime2 = LocalDateTime.of(2022, 10, 28,10,28,0);
        Duration duration1 = Duration.between(dateTime1, dateTime2);
        System.out.println("duration1 = " + duration1);

        LocalTime time1 = LocalTime.of(13,20, 0);
        LocalTime time2 = LocalTime.of(13, 45, 20);
        Duration duration2 = Duration.between(time1, time2);
        System.out.println("duration2 = " + duration2);

        Duration treeMinutes = Duration.ofMinutes(3);
        Duration treeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);
        System.out.println("treeMinutes = " + treeMinutes);
        System.out.println("treeMinutes2 = " + treeMinutes2);

        // 년, 월, 일의 차이는 Period 를 이용한다.
        Period period = Period.between(
                LocalDate.of(2017, 9, 11),
                LocalDate.of(2017, 9, 21)
        );
        System.out.println("period = " + period);

        Period tenDays = Period.ofDays(10);
        Period treeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
    }

    /**
     * TODO : Duration 과 Period 클래스가 공통으로 제공하는 메서드
     * TODO static (정적) :
     *  between
     *  from
     *  of
     *  parse
     *
     * TODO :instant (인스턴스) :
     *  addTo
     *  get
     *  isNegative
     *  isZero
     *  minus
     *  multipliedBy
     *  negated
     *  plus
     *  subtractFrom
     *
     */
}
