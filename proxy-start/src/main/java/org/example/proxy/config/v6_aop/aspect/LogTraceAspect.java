package org.example.proxy.config.v6_aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.Method;

// TODO @Aspect  사용

/**
 * 'AnnotationAwareAspectJAutoProxyConfig' 자동 프록시 생성기 두가지 일
 * 1) 어드바이저기반으로 프록시 생성
 * 2)'@Aspect' 를 찾아 Advisor 로 변환해 저장.
 *      ㄴ==> 이는 스프링이 뜰때 자동 프록시 생성기를 호출하고 자동 프록시 생성기는 스프링 컨테이너에서
 *          ㄴ 모든 @Aspect 를 조회해 이를 기반으로 'BeanFactoryAspectJAdvisorBuilder' 가 Advisor 를 만들어준다.(또한 빌더 내부에 Advisor 를 캐시한다.)
 * ===> 바로 이것이 '횡단 관심사(cross-cutting concerns)' 라 한다.
 */
@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* org.example.proxy.app..*(..))") // -> Pointcut
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // -> Advisor
        // Advice 로직
        // 로그출력
        TraceStatus status = null;

        try {
//            joinPoint.getTarget() //실제 호출 대상
//            joinPoint.getArgs() //전달인자
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // target 호출
            Object result = joinPoint.proceed();

            // 로그 출력
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
    /**
     * 참고로 @Around 메서드는 @Aspect 클래스 한개에 여러개 만들어 줘도 된다.
     */
}
