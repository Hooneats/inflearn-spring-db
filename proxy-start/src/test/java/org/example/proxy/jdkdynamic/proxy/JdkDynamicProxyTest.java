package org.example.proxy.jdkdynamic.proxy;

import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.example.proxy.jdkdynamic.proxy.code.AImpl;
import org.example.proxy.jdkdynamic.proxy.code.AInterface;
import org.example.proxy.jdkdynamic.proxy.code.BImpl;
import org.example.proxy.jdkdynamic.proxy.code.BInterface;
import org.example.proxy.jdkdynamic.proxy.code.TimeInvocationHandler;
import org.junit.jupiter.api.Test;


/**
 * TODO :
 *  동적 프록시 - 자바언어가 지원하는 JDK 동적 프록시
 *   ㄴ 런타임시에 프록시객체를 만들어 준다.
 *   ㄴ JDK 동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들기에 인터페이스는 필수이다.
 *   ㄴ InvocationHandler 를 구현해야한다.
 *              ㄴ 그렇다면 인터페이스가 없는 경우에는? CGLIB 라는 라이브러리를 사용할 수 있다.(오픈소스로 스프링도 사용하고 있다. MethodInterceptor 제공)
 *  // 자바는 클래스들이 호출되면 클래스들이 클래스로더에 올라간다 때문에 클래스로더를 지정해주는것이다.
 *
 *  TODO :
 *   ====> 그렇다면 인터페이스가 있으면 JDK 에 InvocationHandler 를 쓰고
 *   구체 클래스만 있으면 CGLIB 에 MethodInterceptor 를 사용하면될까? 그런데 너무 따로따로 써야하네?
 *              ㄴ 스프링이 제공하는 프록시 팩터리로 해결가능 (ProxyFactory)
 *                ㄴ ProxyFactory 를 사용할때 MethodInterceptor 또는 InvocationHandler 를 Advice 라는 새로운 개념으로 치환하였다.
 *                   ProxyFactory 는 Advice 를 호출하는 용도로 전용 adviceInvocationHandler 나 adviceMethodInterceptor 를 내부에서 사용한다.
 *                   즉, 결과적으로 개발자는 Advice 만 만들면 된다.
 *              ㄴ 또한 앞서(특정 조건 맞을 때 프록시적용) 특정 메서드 이름의 조건에 맞을 때만 프록시 부가 기능이 적용되는 코드를 직접 만들었는데,
 *                  스프링은 이를 Pointcut 이라는 개념을 도입해서 일관성 있게 해결하였다.
 */
@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 자바에서 제공하는 프록시 생성 기술 - 프록시 어디에 생성될지 getClassLoader 필요, 어떤 인터페이스 기반으로 만들지 배열로 지정, 로직지정
        AInterface proxy
            = (AInterface) Proxy.newProxyInstance(
            AInterface.class.getClassLoader(),
            new Class[]{AInterface.class},
            handler
        );
        proxy.call();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 자바에서 제공하는 프록시 생성 기술 - 프록시 어디에 생성될지 getClassLoader 필요, 어떤 인터페이스 기반으로 만들지 배열로 지정, 로직지정
        BInterface proxy
            = (BInterface) Proxy.newProxyInstance(
            BInterface.class.getClassLoader(),
            new Class[]{BInterface.class},
            handler
        );
        proxy.call();
        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }
}
