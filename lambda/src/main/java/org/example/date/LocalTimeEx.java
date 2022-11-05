package org.example.date;

import java.time.LocalTime;

public class LocalTimeEx {

    public static void main(String[] args) {
        LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
        int hour = time.getHour(); // 13
        int minute = time.getMinute(); // 45
        int second = time.getSecond(); // 20

        LocalTime parseTime = LocalTime.parse("13:45:20");
    }
}
