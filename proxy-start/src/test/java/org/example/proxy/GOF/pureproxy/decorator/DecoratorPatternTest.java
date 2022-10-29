package org.example.proxy.GOF.pureproxy.decorator;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.GOF.pureproxy.decorator.code.Component;
import org.example.proxy.GOF.pureproxy.decorator.code.DecoratorPatternClient;
import org.example.proxy.GOF.pureproxy.decorator.code.RealComponent;
import org.example.proxy.GOF.pureproxy.decorator.code.decorator.MessageDecorator;
import org.example.proxy.GOF.pureproxy.decorator.code.decorator.TimeDecorator;
import org.junit.jupiter.api.Test;

/**
 * 데코레이터 패턴 : 기능이 부가되는것(추가)
 * 예 : 요청값이나, 응답 중간에 값을 변형 등
 *      실행 시간 등 추가 로그를 남기는 것 등등
 */
@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);

        client.execute();
    }

    /**
     * RealComponent, Component 와 DecoratorPatternClient 코드를 전혀 건드리지 않고 접근제어 적용이 가능했다.(CacheProxy 적용해봄)
     */
    @Test
    void decorator1() {
        Component component = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(component);
        DecoratorPatternClient client = new DecoratorPatternClient(
            messageDecorator);

        client.execute();
    }

    @Test
    void decorator2() {
        Component component = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(component);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(
            timeDecorator);

        client.execute();
    }
}
