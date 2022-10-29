package org.example.proxy.config.v1_proxy.concrete_proxy;

import org.example.proxy.app.v2.OrderControllerV2;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        // 로그출력
        TraceStatus status = null;

        try {
            status = logTrace.begin("OrderController.request()");

            // target 호출
            String result = target.request(itemId);

            // 로그 출력
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
