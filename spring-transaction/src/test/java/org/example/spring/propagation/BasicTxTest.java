package org.example.spring.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager txManager;

    @TestConfiguration
    static class Config {
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @DisplayName("트랜젝션 커밋")
    @Test
    void commit() {
        log.info("트랜젝션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute()); // Attribute 가 좀 더 기능이 많다.

        log.info("트랜젝션 커밋 시작");
        txManager.commit(status);
        log.info("트랜젝션 커밋 완료");
    }

    @DisplayName("트랜젝션 롤백")
    @Test
    void rollback() {
        log.info("트랜젝션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜젝션 롤백 시작");
        txManager.rollback(status);
        log.info("트랜젝션 롤백 완료");
    }

    @DisplayName("트랜젝션 두번 사용")
    @Test
    void double_commit() {
        log.info("트랜젝션 시작");
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 커밋 시작");
        txManager.commit(tx1);
        log.info("트랜젝션 커밋 완료");

        log.info("트랜젝션 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 커밋 시작");
        txManager.commit(tx2);
        log.info("트랜젝션 커밋 완료");
    }

    @DisplayName("트랜젝션 커밋 and 롤백")
    @Test
    void double_commit_rollback() {
        log.info("트랜젝션 시작");
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 커밋 시작");
        txManager.commit(tx1);
        log.info("트랜젝션 커밋 완료");

        log.info("트랜젝션 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 롤백 시작");
        txManager.rollback(tx2);
        log.info("트랜젝션 롤백 완료");
    }

    @DisplayName("트랜젝션 내부커밋 in 외부커밋")
    @Test
    void inner_commit() {
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction()); // true

        log.info("내부 트랜젝션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        // ***** 외부에 Tx 있기에 false 가 된다. 즉 외부와 내부가 하나로 묶인다는(전파되었다는) 뜻.
        // 트랜젝션 동기화 매니저에 외부 트랜젝션이 생성한 커넥션이 이미 있기에 이를 사용한다.
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction()); // false
        log.info("내부 트랜젝션 커밋 시작");
        // 내부 커밋에 대한 아무런 로그가 찍히지 않는다.
        // 이는 외부 트랜젝션에 참여한 형태(inner.isNewTransaction()=false)이기 때문에 외부 트랜젝션이 커밋할때 같이 일어난다.
        txManager.commit(inner);
        log.info("내부 트랜젝션 커밋 완료");

        log.info("외부 트랜젝션 커밋 시작");
        txManager.commit(outer);
        log.info("외부 트랜젝션 커밋 완료");

        // TODO :
        //  마지막(처음 시작한 isNewTransaction) 논리 트랜젝션(논리중 마지막)의 커밋이 실행될때 전체 (물리) 커넥션을 커밋한다.
        //      ==> 처음 트랜젝션을 시작한 외부 트랜젝션이 실제 전체(물리) 트랜젝션을 관리하기 때문에
    }

    @DisplayName("트랜젝션 내부커밋 in 외부롤백")
    @Test
    void outer_rollback() {
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction()); // true

        log.info("내부 트랜젝션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction()); // false
        log.info("내부 트랜젝션 커밋 시작");
        txManager.commit(inner);
        log.info("내부 트랜젝션 커밋 완료");

        log.info("외부 트랜젝션 롤백 시작");
        txManager.rollback(outer);
        log.info("외부 트랜젝션 롤백 완료");
    }

    // TODO 예외발생
    //  UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
    //  즉 기존 트랜젝션에 내부 트랜젝션이 롤백을 마킹해 놓는다.
    @DisplayName("트랜젝션 내부롤백 in 외부커밋")
    @Test
    void inner_rollback() {
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction()); // true

        log.info("내부 트랜젝션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction()); // false
        log.info("내부 트랜젝션 롤백 시작");
        txManager.rollback(inner); // rollback-only 표시를 하게됨
        log.info("내부 트랜젝션 롤백 완료");

        log.info("외부 트랜젝션 커밋 시작");
        Assertions.assertThatThrownBy(() -> txManager.commit(outer))
                .isInstanceOf(UnexpectedRollbackException.class);
//        txManager.commit(outer);
        log.info("외부 트랜젝션 커밋 완료");
    }

    // TODO
    //  DEBUG 12900 --- [main] o.s.j.d.DataSourceTransactionManager : Suspending current transaction, creating new transaction with name [null]
    //   ㄴ-> 즉 외부에서 만든 커넥션을 잠시 미뤄두고 새로 생성해서 사용한다. (Suspending current transaction)
    //   ㄴ 그러나 이렇게 내부 커넥션을 새로 쓰는것은 프로그램의 커넥션 부족하게 만들 수 있기에 실시간 이용자가 많은경우 조심해야한다.
    @DisplayName("트랜젝션 내부롤백 in 외부커밋, REQUIRES_NEW")
    @Test
    void inner_rollback_requires_new() {
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction()); // true

        log.info("내부 트랜젝션 시작");
        DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus inner = txManager.getTransaction(definition);
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction()); // true

        log.info("내부 트랜젝션 롤백 시작");
        txManager.rollback(inner);
        log.info("내부 트랜젝션 롤백 완료");

        log.info("외부 트랜젝션 커밋 시작");
        txManager.commit(outer);
        log.info("외부 트랜젝션 커밋 완료");
    }
}
