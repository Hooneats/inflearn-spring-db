package org.example.spring.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 스프링은 더 자세하고 구체적인것이 우선순위를 가진다.
 *
 * 추가적으로 인터페이스에도 @Transactional 을 적용할 수 있다.
 *  TODO
 *   @Transactional 적용 우선순위 :
 *      1. 클래스의 메서드(우선순위가 가장 놓다)
 *      2. 클래스의 타입
 *      3. 인터페이스의 메서드
 *      4. 인터페이스의 타입(우선순위가 가장 낮다)
 *      그러나 인터페이스에 트랜젝션적용은 스프링에서 권장하지않는다(적용안되는 경우가 있기에)
 *   ---> 또한 Transaction 을 적용하려면 반드시 스프링이 만든 프록시 객체로 실행해야한다.
 */
@Slf4j
@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService levelService;

    @Test
    void orderTest() {
        log.info("Service {}", levelService.getClass());
        levelService.write();
        levelService.read();
    }

    @TestConfiguration
    static class TxLevelTestConfig{
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {

        @Transactional(readOnly = false)
        public void write() {
            log.info("call write");
            printTxInfo();
        }

        public void read() {
            log.info("call read");
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
