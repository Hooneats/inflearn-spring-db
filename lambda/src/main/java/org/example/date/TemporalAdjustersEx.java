package org.example.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

// TODO : Custom 에 관한 사항은 '모던 자바 인 액션' 책 p.400 참고
public class TemporalAdjustersEx {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2014, 3, 18);// 2014-03-18
        LocalDate date1 = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); // 2014-03-23
        LocalDate date2 = date1.with(TemporalAdjusters.lastDayOfMonth());// 2014-03-31
        System.out.println("date2 = " + date2);
    }
    /**
     * TODO : TemporalAdjusters 클래스의 팩토리 메서드
     *
     * TODO :
     *  dayOfWeekInMonth - 서수 요일에 해당하는 날짜를 반환하는 TemporalAdjuster 를 반환함( 음수를 사용하면 월의 끝에서 거꾸로 계산 )
     *
     * TODO :
     *  firstDayOfMonth - 현재 달의 첫 번째 날짜를 반환하는 TemporalAdjuster 를 반환함
     *  firstDayOfNextMonth - 다음 달의 첫 번째 날짜를 반환하는 TemporalAdjuster 를 반환함
     *  firstDayOfNextYear - 내년의 첫 번째 날짜를 반환하는 TemporalAdjuster 를 반환함
     *  firstDayOfYear - 올해의 첫 번째 날짜를 반환하는 TemporalAdjuster 를 반환함
     *  firstInMonth -  현재 달의 첫 번째 요일에 해당하는 날짜를 반환하는 TemporalAdjuster 를 반환함
     *
     * TODO :
     *  lastDayOfMonth - ''
     *  lastDayOfNextMonth - ''
     *  lastDayOfNextYear - ''
     *  lastDayOfYear - ''
     *  lastInMonth - ''
     *
     * TODO :
     *  next -
     *  previous -
     *  nextOrSame -
     *  previousOrSame -
     */
}
