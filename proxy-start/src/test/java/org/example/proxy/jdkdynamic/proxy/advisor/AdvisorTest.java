package org.example.proxy.jdkdynamic.proxy.advisor;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.jdkdynamic.proxy.cglib.common.ServiceImpl;
import org.example.proxy.jdkdynamic.proxy.cglib.common.ServiceInterface;
import org.example.proxy.jdkdynamic.proxy.cglib.common.advice.TimeAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

// TODO 어드바이저는 하나의 어드바이스(부가기능) 와 하나의 포인트컷(어디에) 둘다 가지고 있다.
// TODO : ProxyFactory 는 Advisor 를 알고 있고 Advisor 는 Advice(무엇을)  와 Pointcut(어디에) 을 알고 있다.
@Slf4j
public class AdvisorTest {

    @Test
    @DisplayName("포인트컷 어드바이스 디폴트어드바이저")
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

    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find(); // 부가기능(Advice 인 TimeAdvice) 적용 안됨
    }

    static class MyPointcut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher {

        public static final String MATCH_NAME = "save";

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(MATCH_NAME);
            log.info("포인트컷 호출 method = {}, targetClass = {}", method.getName(), targetClass);
            log.info("포인트컷 결과 result = {}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }



    @Test
    @DisplayName("스프링이 제공하는 것 중 하나의 포인트컷")
    void advisorTest3() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save"); // 메서드 이름이 save 인 경우
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find(); // 부가기능(Advice 인 TimeAdvice) 적용 안됨
    }
    /**
     * TODO : 스프링이 제공하는 포인트컷 몇가지만
     *  NameMatchMethodPointcut
     *  JdkRegexMethodPointcut
     *  TruePointcut
     *  AnnotationMatchingPointcut
     *  AspectJExpressionPointcut -> 제일 중요하다 실무에서는 이것이 사용되는 줄도 모르고 쓸정도로 많이씀
     */

}
