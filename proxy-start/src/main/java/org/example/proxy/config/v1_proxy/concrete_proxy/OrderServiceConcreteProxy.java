package org.example.proxy.config.v1_proxy.concrete_proxy;

import org.example.proxy.app.v2.OrderServiceV2;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null); // 위의(실제) 기능을 안쓰로 프록시 기능만 사용하기에 null (인터페이스 안쓰면 이런 단점이 있다)
        this.target = target;
        this.logTrace = logTrace;
    }


    @Override
    public void orderItem(String itemId) {
        // 로그출력
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderService.orderItem()");

            // target 호출
            target.orderItem(itemId);

            // 로그 출력
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }

    }
}
