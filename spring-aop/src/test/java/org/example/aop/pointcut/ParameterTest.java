package org.example.aop.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.aop.member.MemberService;
import org.example.aop.member.annotation.ClassAop;
import org.example.aop.member.annotation.MethodAop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("hello");
    }

    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* org.example.aop.member..*.*(..))")
        private void allMember() {

        }

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{} , arg1={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{} , arg2={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) throws Throwable {
            log.info("[logArgs3] , arg3={}", arg);
        }

        @Before("allMember() && this(obj)") // 프록시 객체 전달 받는다. 때문에 JDK 인경우 인터페이스(프록시가 만약 impl 이면 받지 못함)를  CGLIB 인경우 구체클래스(부모는 가능하기에 CGLIB 는 interface 를 안다)를
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this] {} , obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)") // 실제 대상 (인스턴스) 전달 받는다.
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target] {} , obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && @target(annotation)") // 에노테이션 전달 받는다.
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target] {} , obj = {}", joinPoint.getSignature(), annotation.getClass());
        }

        @Before("allMember() && @within(annotation)") // 에노테이션 전달 받는다.
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within] {} , obj = {}", joinPoint.getSignature(), annotation.getClass());
        }

        @Before("allMember() && @annotation(annotation)") // 에노테이션 value
        public void atannotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation] {} , annotationValue = {}", joinPoint.getSignature(), annotation.value());
        }
    }

}
