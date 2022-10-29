package org.example.proxy.config.v1_proxy.interface_proxy;

import lombok.RequiredArgsConstructor;
import org.example.proxy.app.v1.OrderRepositoryV1;
import org.example.proxy.trace.TraceStatus;
import org.example.proxy.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private final OrderRepositoryV1 target;
    private final LogTrace logTrace;

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
