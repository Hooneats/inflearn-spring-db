package org.example.proxy.jdkdynamic.proxy.cglib.common.advice.proxyfactory;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.proxy.jdkdynamic.proxy.cglib.common.ConcreteService;
import org.example.proxy.jdkdynamic.proxy.cglib.common.ServiceImpl;
import org.example.proxy.jdkdynamic.proxy.cglib.common.ServiceInterface;
import org.example.proxy.jdkdynamic.proxy.cglib.common.advice.TimeAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {

    @DisplayName("인터페이스가 있으면 ProxyFactory 는 자동으로 JDK 동적 프록시 사용")
    @Test
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();

        // ProxyFactory 를 만들때 target 을 미리 넣어 Advice 에서는 proceed 만 하면된다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass()); // .$Proxy10

        proxy.save();

        // ProxyFactory 를 사용한다면 AopUtils 를 사용할 수 있다.
        boolean isProxy = AopUtils.isAopProxy(proxy);
        boolean isJdkDynamicProxy = AopUtils.isJdkDynamicProxy(proxy);
        boolean isCglibProxy = AopUtils.isCglibProxy(proxy);
        assertThat(isProxy).isTrue();
        assertThat(isJdkDynamicProxy).isTrue();
        assertThat(isCglibProxy).isFalse();
    }

    @DisplayName("구체 클래스만 있으면 CGLIB 사용하게 됨")
    @Test
    void concreteProxy() {
        ConcreteService target = new ConcreteService();

        // ProxyFactory 를 만들때 target 을 미리 넣어 Advice 에서는 proceed 만 하면된다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass()); // ConcreteService$$EnhancerBySpringCGLIB$$52146047

        proxy.call();

        // ProxyFactory 를 사용한다면 AopUtils 를 사용할 수 있다.
        boolean isProxy = AopUtils.isAopProxy(proxy);
        boolean isCglibProxy = AopUtils.isCglibProxy(proxy);
        boolean isJdkDynamicProxy = AopUtils.isJdkDynamicProxy(proxy);
        assertThat(isProxy).isTrue();
        assertThat(isCglibProxy).isTrue();
        assertThat(isJdkDynamicProxy).isFalse();
    }

    /**
     * TODO :
     *  스프링 부트는 AOP 를 적용할 때 기본적으로 'proxyTargetClass=true' 옵션을 사용한다.
     *  따라서 인터페이스가 있어도 항상 CGLIB 를 사용해서 구체 클래스를 기반으로 프록시를 생성한다.
     */
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 를 사용하고, 클래스 기반 프록시 사용")
    @Test
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();

        // ProxyFactory 를 만들때 target 을 미리 넣어 Advice 에서는 proceed 만 하면된다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass()); // ServiceImpl$$EnhancerBySpringCGLIB$$6fc30e30

        proxy.save();

        // ProxyFactory 를 사용한다면 AopUtils 를 사용할 수 있다.
        boolean isProxy = AopUtils.isAopProxy(proxy);
        boolean isCglibProxy = AopUtils.isCglibProxy(proxy);
        boolean isJdkDynamicProxy = AopUtils.isJdkDynamicProxy(proxy);
        assertThat(isProxy).isTrue();
        assertThat(isCglibProxy).isTrue();
        assertThat(isJdkDynamicProxy).isFalse();
    }
}
