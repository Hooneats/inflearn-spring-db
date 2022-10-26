package org.example.spring.trace.template;

import org.example.spring.trace.TraceStatus;
import org.example.spring.trace.logtrace.threadlocal.LogTrace;

public abstract class AbstractTemplate<T> {

    private final LogTrace trace;


    protected AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);

            //로직 호축
            T result = call();

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }

    }

    protected abstract T call();
}
