package org.example.spring.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO :
 *  스프링은
 *      체크예외 : 브즈니스 의미가 있어 개발자가 복구 로직을 처리할 예외로 생각해 기본적으로 롤백하지 않는다.
 *      언체크예외 : 개발자가 의도하지않은 예외로 보고 복구시도, 즉 롤백한다.
 */
@Slf4j
@SpringBootTest
public class RollbackTest {

    @Autowired
    RollbackService service;

    @DisplayName("런타임 예외 발생 : 롤백")
    @Test
    void runtimeException() {
        // 결과 : DEBUG 23204 --- [main] o.s.orm.jpa.JpaTransactionManager : Initiating transaction rollback
        // DEBUG 23204 --- [main] o.s.orm.jpa.JpaTransactionManager : Rolling back JPA transaction on EntityManager [SessionImpl(265108079<open>)]
        Assertions.assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("체크 예외 발생 : 커밋")
    @Test
    void checkedException() {
        // 결과 : DEBUG 2884 --- [main] o.s.orm.jpa.JpaTransactionManager : Initiating transaction commit
        Assertions.assertThatThrownBy(() -> service.checkedException())
                .isInstanceOf(MyException.class);
    }

    @DisplayName("체크 예외 rollbackFor 강제 롤백")
    @Test
    void rollbackFor() {
        // 결과 : DEBUG 22008 --- [main] o.s.orm.jpa.JpaTransactionManager : Initiating transaction rollback
        Assertions.assertThatThrownBy(() -> service.rollbackFor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollbackTestConfig {
        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    static class RollbackService {

        // 런타임 예외 발생 : 롤백
        @Transactional
        public void runtimeException() {
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        // 체크 예외 발생 : 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        // 체크 예외 rollbackFor 강제 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }
    }

    static class MyException extends Exception {
    }
}
