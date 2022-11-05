package org.example.proxy.jdkdynamic.proxy.advisor;

import org.example.proxy.jdkdynamic.proxy.cglib.common.ServiceImpl;
import org.example.proxy.jdkdynamic.proxy.cglib.common.ServiceInterface;
import org.example.proxy.jdkdynamic.proxy.cglib.common.advice.TimeAdvice;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

// TODO 어드바이저는 하나의 어드바이스(부가기능) 와 하나의 포인트컷(어디에) 둘다 가지고 있다.
// TODO : ProxyFactory 는 Advisor 를 알고 있고 Advisor 는 Advice 와 Pointcut 을 알고 있다.
public class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // Pointcut.True 는 항상 참인 Pointcut
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }
}
