package org.example.spring.app.template.v4;

import lombok.RequiredArgsConstructor;
import org.example.spring.trace.TraceStatus;
import org.example.spring.trace.logtrace.threadlocal.LogTrace;
import org.example.spring.trace.template.FunctionTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        FunctionTemplate<Void> template = () -> {
            orderRepository.save(itemId);
            return null;
        };
        template.execute("OrderService.orderItem()", trace);
    }
}
