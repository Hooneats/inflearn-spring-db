package org.example.proxy.config.v1_proxy.concrete_proxy;

import org.example.proxy.app.v2.OrderRepositoryV2;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;

public class OrderRepositoryConcreteProxy extends OrderRepositoryV2 {

    private final OrderRepositoryV2 target;
    private final LogTrace logTrace;

    public OrderRepositoryConcreteProxy(OrderRepositoryV2 target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void save(String itemId) {
        // 로그출력
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderRepository.save()");

            // target 호출
            target.save(itemId);

            // 로그 출력
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
