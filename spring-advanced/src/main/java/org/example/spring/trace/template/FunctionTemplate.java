package org.example.spring.trace.template;

import org.example.spring.trace.TraceStatus;
import org.example.spring.trace.logtrace.threadlocal.LogTrace;

@FunctionalInterface
public interface FunctionTemplate<R> {

    default R execute(String message, LogTrace trace) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            //로직 호축
            R result = call();

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    abstract R call();
}
