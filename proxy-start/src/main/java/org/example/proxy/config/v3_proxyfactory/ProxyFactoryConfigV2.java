package org.example.proxy.config.v3_proxyfactory;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.app.v2.OrderControllerV2;
import org.example.proxy.app.v2.OrderRepositoryV2;
import org.example.proxy.app.v2.OrderServiceV2;
import org.example.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import org.example.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO ProxyFactory 는 인터페이스가 아닌 구체클래스를 활용할 경우 CGLIB  => $$EnhancerBySpringCGLIB$$
@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

    @Bean
    public OrderControllerV2 orderControllerV2(LogTrace logTrace) {
        OrderControllerV2 orderControllerV2 = new OrderControllerV2(orderServiceV2(logTrace));

        // advisor 적용
        ProxyFactory factory = new ProxyFactory(orderControllerV2);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderControllerV2 proxy = (OrderControllerV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderControllerV2.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
        OrderServiceV2 orderServiceV2 = new OrderServiceV2(orderRepository2(logTrace));

        // advisor 적용
        ProxyFactory factory = new ProxyFactory(orderServiceV2);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderServiceV2 proxy = (OrderServiceV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderServiceV2.getClass());
        return proxy;
    }

    @Bean
    public OrderRepositoryV2 orderRepository2(LogTrace logTrace) {
        OrderRepositoryV2 orderRepositoryV2 = new OrderRepositoryV2();

        // advisor 적용
        ProxyFactory factory = new ProxyFactory(orderRepositoryV2);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryV2 proxy = (OrderRepositoryV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderRepositoryV2.getClass());
        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        //advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
