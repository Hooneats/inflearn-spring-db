package org.example.spring.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.trace.strategy.code.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

@Slf4j
public class ContextV2Test {


    @DisplayName("전략 패턴 사용 V2")
    @Test
    void strategyV2_1() {
        ContextV2 contextV2 = new ContextV2();
        contextV2.execute(new StrategyLogic1());
        contextV2.execute(new StrategyLogic2());
    }

    @DisplayName("전략 패턴 사용 V2 익명내부클래스")
    @Test
    void strategyV2_2() {
        ContextV2 contextV2 = new ContextV2();
        contextV2.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 1 실행");
            }
        });
        contextV2.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 2 실행");
            }
        });
    }

    @DisplayName("전략 패턴 사용 V2 람다")
    @Test
    void strategyV2_3() {
        ContextV2 contextV2 = new ContextV2();
        contextV2.execute(() -> log.info("비즈니스 로직 1 실행"));
        contextV2.execute(() -> log.info("비즈니스 로직 2 실행"));
    }

    @Test
    void lamdaAndThenTest() {
        Consumer<StringBuilder> con = (v) -> v.append("asd");
        Consumer<StringBuilder> then = con.andThen(s -> s.append("weqrqw"))
                .andThen(s -> System.out.println(s.toString()));
        then.accept(new StringBuilder("시작"));
    }
}
