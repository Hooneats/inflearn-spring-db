package org.example.proxy.jdkdynamic.proxy.cglib.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * ProxyFactory
 * Advice => aopalliance 패키지의 MethodInterceptor
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long start = System.currentTimeMillis();

        // -> target 이 된다 즉 invocation 에 담아두었다가 proceed 호출하면 알아서 target 을 찾아 실행한다.
        Object result = invocation.proceed();

        long end = System.currentTimeMillis();
        log.info("실행 시간 = {}", end - start);
        return result;
    }
}
