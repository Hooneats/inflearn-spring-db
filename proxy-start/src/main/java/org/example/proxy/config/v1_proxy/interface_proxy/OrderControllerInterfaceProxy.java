package org.example.proxy.config.v1_proxy.interface_proxy;

import lombok.RequiredArgsConstructor;
import org.example.proxy.app.v1.OrderControllerV1;
import org.example.proxy.app.v1.OrderRepositoryV1;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {

    private final OrderControllerV1 target;
    private final LogTrace logTrace;

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
