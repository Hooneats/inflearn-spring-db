package org.example.spring.app.callback.v5;

import org.example.spring.trace.callback.TraceTemplate;
import org.example.spring.trace.logtrace.threadlocal.LogTrace;
import org.example.spring.trace.template.FunctionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate traceTemplate;

    public OrderRepositoryV5(LogTrace logTrace) {
        this.traceTemplate = new TraceTemplate(logTrace);
    }


    public void save(String itemId) {

        traceTemplate.execute(
                "OrderRepository.save()",
                () -> {
                    if (itemId.equals("ex")) {
                        throw new IllegalStateException("예외 발생");
                    }
                    sleep(1000);
                    return null;
                }
        );
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
