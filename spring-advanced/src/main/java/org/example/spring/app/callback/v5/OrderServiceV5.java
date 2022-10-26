package org.example.spring.app.callback.v5;

import org.example.spring.trace.callback.TraceTemplate;
import org.example.spring.trace.logtrace.threadlocal.LogTrace;
import org.example.spring.trace.template.FunctionTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate traceTemplate;

    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace logTrace) {
        this.orderRepository = orderRepository;
        this.traceTemplate = new TraceTemplate(logTrace);
    }


    public void orderItem(String itemId) {

        traceTemplate.execute(
                "OrderService.orderItem()",
                () -> {
                    orderRepository.save(itemId);
                    return null;
                }
        );
    }
}
