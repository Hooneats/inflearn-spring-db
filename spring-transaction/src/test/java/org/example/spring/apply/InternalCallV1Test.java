package org.example.spring.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * TODO :
 *  프록시 객체의 internal 메서드 문제
 *   - 인터널 메서드에는 트랜젝션이 안된다. -> 클래스로 분리해 프록시 체 진입 메서드가 트랜젝션있는 메서드이도록 해결가능
 *   - public 메서드만 트랜젝션이 적용된다.
 *       ㄴ-- 트랜젝션은 비즈니스의 시작점에 거는 경우가 많다 그렇기에 public 에만 걸리도록 스프링에서 구현했다.
 *   - 스프링이 초기화하는 시점에는 AOP 가 적용되지 않을 수 있다.
 *       ㄴ-- @PostConstructor 같은 경우를 말한다. - 초기화가 먼저 실행되고 AOP 가 실행되기에 ->  @EventListener(ApplicationReadyEvent.class) 해결가능
 */
@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy() {
        log.info("callService class = {}", callService.getClass());
    }

    @DisplayName("프록시 객체로 실행 메서드가 @Transactional 을 가지고 있어야 트랜젝션이 적용된다.")
    @Test
    void internalCall() {
        callService.internal();
    }

    @DisplayName("프록시 객체여도 실행 메서드가 @Transactional 을 시작하지 않으면 트랜젝션 적용이 안된다.")
    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig{
        @Bean
        CallService callService() {
            return new CallService();
        }
    }

    static class CallService{

        public void external() {
            log.info("call external");
            printTxInfo();
            internal();
        }

        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);

            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", readOnly);

        }
    }

}
