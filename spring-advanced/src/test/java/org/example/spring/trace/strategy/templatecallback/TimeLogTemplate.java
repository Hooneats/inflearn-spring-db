package org.example.spring.trace.strategy.templatecallback;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.trace.strategy.code.Strategy;

@Slf4j
public class TimeLogTemplate {
    public void execute(Callback callback) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        callback.call(); // 변하는 부분
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("result Time = {}", resultTime);
    }
}
