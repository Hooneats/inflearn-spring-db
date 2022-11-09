package org.example.aop.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.aop.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

// TODO : Spring Boot 는 기본으로 CGLIB 로만 만들게 되어있다.

/**
 * TODO
 *  spring.aop.proxy-target-class=true   : CGLIB(Spring Boot 의 Default)
 *  spring.aop.proxy-target-class=false  : JDK 동적 프록스
 */
@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
@SpringBootTest(properties = "spring.aop.proxy-target-class=false")
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}, class = {}", AopUtils.isAopProxy(memberService), memberService.getClass());
        memberService.hello("helloA");
    }

    @Aspect
    static class ThisTargetAspect {

        // this 는 부모타입 허용
        @Around("this(org.example.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // this 는 부모타입 허용
        @Around("target(org.example.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // this 는 부모타입 허용
        @Around("this(org.example.aop.member.MemberServiceImpl)")
        public Object doThisImpl(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // this 는 JDK 동적 프록시에서 구체타입 을 잡지 못한다.
        @Around("target(org.example.aop.member.MemberServiceImpl)")
        public Object doTargetImpl(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl] {}", joinPoint.getSignature()); // 결과 : 로그가 찍히지 않음
            return joinPoint.proceed();
        }
    }
}
