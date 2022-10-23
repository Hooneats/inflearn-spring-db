package org.example.spring.app.v1;

import lombok.RequiredArgsConstructor;
import org.example.spring.trace.TraceStatus;
import org.example.spring.trace.hellotrace.HelloTraceV1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace;

    @GetMapping("/v1/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 서비스상 예외를 꼭 다시 던져주어야 한다. - 로그는 서비스 흐름에 영향을 주면 안되기에 로그로인해 정상흐름이 되면 안된다.
        }

        return "ok";
    }
}
