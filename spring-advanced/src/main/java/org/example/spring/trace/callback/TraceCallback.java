package org.example.spring.trace.callback;

public interface TraceCallback<T> {
    T call();
}
