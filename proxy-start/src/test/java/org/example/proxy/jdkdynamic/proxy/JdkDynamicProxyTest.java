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
 *              ㄴ 그렇다면 인터페이스가 없는 경우에는? CGLIB 라는 라이브러리를 사용할 수 있다.
 *  // 자바는 클래스들이 호출되면 클래스들이 클래스로더에 올라간다 때문에 클래스로더를 지정해주는것이다.
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
