package org.example.spring.trace.strategy.templatecallback;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackTest {

    @DisplayName("템플릿 콜백 패턴 익명 내부 클래스")
    @Test
    void callbackV1() {
        TimeLogTemplate logTemplate = new TimeLogTemplate();
        logTemplate.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직 1 실행");
            }
        });
        logTemplate.execute(new Callback() {
            @Override
            public void call() {
                log.info("비즈니스 로직 2 실행");
            }
        });
    }

    @DisplayName("템플릿 콜백 패턴 람다")
    @Test
    void callbackV2() {
        TimeLogTemplate logTemplate = new TimeLogTemplate();
        logTemplate.execute(() -> log.info("비즈니스 로직 1 실행"));
        logTemplate.execute(() -> log.info("비즈니스 로직 2 실행"));
    }
}
