package org.example.proxy.config.v2_dynamicproxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

/**
 * no-log 인 조건일때는 logTrace 를 호출하지 않도록 pattern 을 잡음 --
 * TODO : Spring 에 PatternMatchUtils 가 있다.
 */
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] pattern;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] pattern) {
        this.target = target;
        this.logTrace = logTrace;
        this.pattern = pattern;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 메서드 이름 필터
        String methodName = method.getName();
        // save , save* , *save , *save*
        if (!PatternMatchUtils.simpleMatch(pattern, methodName)) {
            return method.invoke(target, args); // 로그없이 실제 메서드 실행하고 끝
        }

        // 로그출력
        TraceStatus status = null;

        try {
            String message = method.getDeclaringClass().getSimpleName()
                + "."
                + method.getName()
                + "()";
            status = logTrace.begin(message);

            // target 호출
            Object result = method.invoke(target, args);

            // 로그 출력
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
