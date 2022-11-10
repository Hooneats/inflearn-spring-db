package org.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.aop.order.OrderRepository;
import org.example.aop.order.OrderService;
import org.example.aop.order.aop.*;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
//@Import(AspectV1.class) // @Import 사용하면 스프링 빈으로 등록해준다. -> Aspect 빌더에서 포인트컷이랑 어드바이스를 만들어준다.
//@Import(AspectV2.class) // 어드바이스 추가
//@Import(AspectV3.class) // 트랜젝션 추가
//@Import(AspectV4Pointcut.class) // 외부 포인트컷 관리
//@Import({AspectV5Order.TransactionAspect.class, AspectV5Order.LogAspect.class}) // 포인트컷 순서 적용
@Import(AspectV6Advice.class) // 다양한 에노테이션
public class AopTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    // TODO : 스프링은 AOP 기술 + 자동 프록시 생성기를 통해 프록시를 만든다.
    //  target 인스턴스는 빈으로 등록되지는 않지만 의존관계 주입은 받을 수 있습니다.
    //  빈으로 등록되는것은 프록시이다.
    @Test
    void aopInfo() {
        log.info("isAopProxy , orderService = {}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy , orderRepository = {}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
        Assertions.assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }
}
