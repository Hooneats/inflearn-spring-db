package org.example.date;

import java.time.Instant;
import java.time.temporal.ChronoField;

/**
 * 기계의 날짜와 시간
 * Instant 는 기계적인 관점에서 시간을 표현한것, 유닉스 에포크 시간을 기준으로 특정 지점까지의 시간을 초로 표현한다.
 * 유닉스 에포크 시간 : 1970년 01월 01일 0시 0분 0초 UTC
 * Instant 는 나노초(10억분의 1초) 의 정밀도를 제공한다. (초와 나노초 정보를 포함)
 * 기계전용 유틸로 사람이 읽을 수 있는 정보를 제공하지 않는다.
 */
public class InstantEx {

    public static void main(String[] args) {

        Instant.ofEpochSecond(3);
        Instant.ofEpochSecond(3, 0);
        Instant.ofEpochSecond(2, 1_000_000_000); // 2초 이후의 1억 나노초(1초)
        Instant.ofEpochSecond(4, -1_000_000_000); // 4초 이전의 1억 나노초(1초)

//        int day = Instant.now().get(ChronoField.DAY_OF_MONTH);
//        System.out.println("day = " + day); // -> 에러 사람이 읽을 수 있는 정보를 제공하지 않기에
        // ---> Instant 는 Duration 과 Period 와 함께 사용해 주로 활용한다.


    }
}
