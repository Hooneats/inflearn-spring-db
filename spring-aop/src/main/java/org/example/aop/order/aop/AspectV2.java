package org.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

// TODO : Around 와 Pointcut 분리
@Slf4j
@Aspect
public class AspectV2 {

    @Pointcut("execution(* org.example.aop.order..*(..))")
    private void allOrder() {
        // 이 방식을 pointcut signature 라고 하며 의미부여 및 여러군데에서 이 포인트컷을 재사용가능하게된다.
    }

    @Around("allOrder()") // order.. -> order 하위에있는거 전부
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처(메서드 정보들)
        return joinPoint.proceed(); // target 호출
    }
}
