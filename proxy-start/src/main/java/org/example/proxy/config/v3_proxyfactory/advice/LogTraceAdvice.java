package org.example.proxy.config.v3_proxyfactory.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.Method;

// TODO 스프링이 제공하는 어드바이스 MethodInterceptor
public class LogTraceAdvice implements MethodInterceptor {

    private final LogTrace logTrace;

    public LogTraceAdvice(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 로그출력
        TraceStatus status = null;

        try {
            Method method = invocation.getMethod();
            String message = method.getDeclaringClass().getSimpleName()
                    + "."
                    + method.getName()
                    + "()";
            status = logTrace.begin(message);

            // target 호출
            Object result = invocation.proceed();

            // 로그 출력
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

}
