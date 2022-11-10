package org.example.aop.exam.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.aop.exam.annotation.Retry;

@Slf4j
@Aspect
public class RetryAspect {

    @Around("@annotation(retry)") // == "@annotation(org.example.aop.exam.annotation.Retry)"
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[retry] {}, @Retry = {}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count = {} , maxRetry = {}", retryCount, maxRetry);
                return joinPoint.proceed(); // 성공하면 타겟 로직 실행후 끝
            } catch (Exception e) {
                exceptionHolder = e; // 실패하면 exceptionHolder 에다 담기만하고 정상처리 후 다시 재시도
            }
        }
        // Exception 홀더가 넘어가면 Exception 던져야함
        throw exceptionHolder;
    }
}
