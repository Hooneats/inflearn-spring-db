package org.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
public class AspectV6Advice {
    /**
     * JoinPoint 가 가진 메서드
     * - getArgs() 메서드 인수를 반환
     * - getThis() 프록시 객체를 반환
     * - getTarget() 대상 객체를 반환
     * - getSignature() 조언되는 메서드에 대한 설명 반환
     * - toString() 조언되는 방법에 대한 유용한 설명을 인쇄
     *
     * ProceedingJoinPoint 가 가진 기능
     * - proceed() 다음 어드바이스나 타겟을 호출
     * ===> @Around 사용시에는 proceed() 반드시 호출해야 타겟이 호출된다.
     */

    /**
     * TODO : 순서
     *   클라이언트 ----- @Around ---- @Before ---- @After ---- @AfterReturning ---- @AfterThrowing ---- Target
     *                    1             2                                                                3
     *                    7                         6                5                    4
     */
    @Around("org.example.aop.order.aop.Pointcuts.orderAndService()") // 가장 강력! 아래처럼 모든게 가능
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
//                @Before // 조인 포인트 실행 이전에 실행
            log.info("[트랜젝션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
//                @AfterReturning // 조인 포인트가 정상 완료 후 실행
            log.info("[트랜젝션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
//                @AfterThrowing // 메서드가 예외를 던지는 경우 실행
            log.info("[트랜젝션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
//                @After // 조인 포인트가 정상 또는 예외에 관계없이 실행(finally)
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    @Before("org.example.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "org.example.aop.order.aop.Pointcuts.orderAndService()", returning = "resultCustom")
    public void doReturn(JoinPoint joinPoint, Object resultCustom) {
        // target 이 뱉은 return 값을 볼 수 있다. 그러나 return 의 값을 변경은 불가능하다.(단 조작은 가능)
        // 만약 값을 변경하고 싶다면 @Around 를 사용해야 한다.
        log.info("[return] {} , return = {}", joinPoint.getSignature(), resultCustom);
    }

    @AfterThrowing(value = "org.example.aop.order.aop.Pointcuts.orderAndService()", throwing = "exCustom")
    public void doThrowing(JoinPoint joinPoint, Exception exCustom) {
        log.info("[ex] {}, message = {}", exCustom.getClass(), exCustom.getMessage());
    }

    @After(value = "org.example.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        // 일반적으로 리소스를 해제하는데 사용된다.
        log.info("[after] {}", joinPoint.getSignature());
    }
}
