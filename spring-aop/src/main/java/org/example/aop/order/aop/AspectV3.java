package org.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.DriverManager;

// TODO : Around 와 Pointcut 분리
//        트랜젝션을 적용해보자
@Slf4j
@Aspect
public class AspectV3 {

    @Pointcut("execution(* org.example.aop.order..*(..))")
    private void allOrder() {
        // 이 방식을 pointcut signature 라고 하며 의미부여 및 여러군데에서 이 포인트컷을 재사용가능하게된다.
    }

    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() { // 클래스 이름 패턴이 *Service 인것
    }

    @Around("allOrder()") // order.. -> order 하위에있는거 전부
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처(메서드 정보들)
        return joinPoint.proceed(); // target 호출
    }

    /**
     * TODO : 추가적인 스프링의 Transaction 동작 방식
     * 추가적인 트랜젝션 Jdbc 모듈 @MemberServiceV3_1 , @MemberServiceV3_2 , @MemberServiceV3_3 참고
     *  * TODO
     *  *    [PlatformTransactionManager]
     *  *          트랜젝션 매니저 -----(1.커넥션생성 2.autoCommit(false) 해 커넥션 보냄)------> 트랜젝션 동기화 매니저 (커넥션 관리)
     *  *              |                                                                           |(커넥션 획득 및 트랜젝션 커넥션 동기화)
     *  *          서비스 로직 시작(트랜젝션 시작) -----------------------> 비즈니스 로직 ----------------> 데이터 접근 로직
     *  *              [AOP 프록시]                                    [서비스]                        [레포지토리]
     */
    // org.example.aop.order 패키지와 하위 패키지 이면서 동시에 클래스 이름이 *Service 인것
    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        PlatformTransactionManager transactionManager = getTransactionTemplate().getTransactionManager();
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("[트랜젝션 시작] {}", joinPoint.getSignature());
        try {
            Object result = joinPoint.proceed();
            log.info("[트랜젝션 커밋] {}", joinPoint.getSignature());
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            log.info("[트랜젝션 롤백] {}", joinPoint.getSignature());
            transactionManager.rollback(status);
            throw e;
        } finally {
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            // transactionManager 가 commit 또는 rollback 후에 알아서 release(con.autoCommit(true) AND con.close()) 해준다.
        }
    }

    private TransactionTemplate getTransactionTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                "jdbc:h2:mem:testdb", "sa", ""
        );
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        return transactionTemplate;
    }
}
