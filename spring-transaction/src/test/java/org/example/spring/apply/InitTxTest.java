package org.example.spring.apply;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;

    @DisplayName("초기화 코드는 스프링이 초기화 시점에 호출한다.")
    @Test
    void go() {
        // 스프링이 그냥 실행만하면 알아서 초기화 시키는중
        // 결과 :
        // INFO 12477 --- [main] org.example.spring.apply.InitTxTest : @PostConstructor tx = false
        // TRACE 12477 --- [main] o.s.t.i.TransactionInterceptor : Getting transaction for [org.example.spring.apply.InitTxTest$Hello.initV2]
        // INFO 12477 --- [main] org.example.spring.apply.InitTxTest : @EventListener tx = true
    }

    @TestConfiguration
    static class InitTxTestConfig {

        @Bean
        Hello hello() {
            return new Hello();
        }
    }

    static class Hello {

        @PostConstruct
        @Transactional
        public void initV1() {
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("@PostConstructor tx = {}", active);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("@EventListener tx = {}", active);
        }
    }

}
