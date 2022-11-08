package org.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

// TODO : Around 와 Pointcut 분리
//        공용 포인트컷 적용
//        순서 적용 - 클래스 단위로만 가능
@Slf4j
public class AspectV5Order { // 껍데기

    @Aspect
    @Order(2)
    public static class LogAspect {
        @Around("org.example.aop.order.aop.Pointcuts.allOrder()") // order.. -> order 하위에있는거 전부
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처(메서드 정보들)
            return joinPoint.proceed(); // target 호출
        }
    }

    @Aspect
    @Order(1)
    public static class TransactionAspect {
        @Around("org.example.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[트랜젝션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜젝션 커밋] {}", joinPoint.getSignature());
                return result;
            } catch (Exception e) {
                log.info("[트랜젝션 롤백] {}", joinPoint.getSignature());
                throw e;
            } finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }





}
