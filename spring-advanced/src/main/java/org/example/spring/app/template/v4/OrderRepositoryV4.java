package org.example.spring.app.template.v4;

import lombok.RequiredArgsConstructor;
import org.example.spring.trace.TraceStatus;
import org.example.spring.trace.logtrace.threadlocal.LogTrace;
import org.example.spring.trace.template.FunctionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

    private final LogTrace trace;

    public void save(String itemId) {

        FunctionTemplate<Void> template = () -> {
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생");
            }
            sleep(1000);
            return null;
        };
        template.execute("OrderRepository.save()", trace);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
