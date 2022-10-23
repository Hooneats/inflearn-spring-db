package org.example.spring.app.v2;

import lombok.RequiredArgsConstructor;
import org.example.spring.trace.TraceId;
import org.example.spring.trace.TraceStatus;
import org.example.spring.trace.hellotrace.HelloTraceV1;
import org.example.spring.trace.hellotrace.HelloTraceV2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final HelloTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId) {

        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            orderRepository.save(status.getTraceId(), itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
