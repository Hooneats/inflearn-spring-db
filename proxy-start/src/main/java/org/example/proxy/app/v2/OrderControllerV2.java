package org.example.proxy.app.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping//스프링은 @Controller 또는 @RequestMapping 이 있어야 스프링 컨트롤러로 인식한다.
// -> @Controller 는 내부에 @Component 도 있기에 우리는 수동등록을 하기위해 @RequestMapping 사용
@ResponseBody
public class OrderControllerV2 {
    private final OrderServiceV2 orderService;

    public OrderControllerV2(OrderServiceV2 orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/v2/no-log")
    public String noLog() {
        return null;
    }
}
