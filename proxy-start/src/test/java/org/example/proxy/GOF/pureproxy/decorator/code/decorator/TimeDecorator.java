package org.example.proxy.GOF.pureproxy.decorator.code.decorator;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.GOF.pureproxy.decorator.code.Component;

// TODO : 데코레이터 패턴 - 체인이 가능하다
// TODO : Decorator 적용 전 = Client -------> Decorator -------> RealComponent
// TODO : Decorator 적용 후 = Client -------> Decorator1 ------> Decorator2 -------> RealComponent
@Slf4j
public class TimeDecorator implements Component {

    private final Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");

        long start = System.currentTimeMillis();

        String result = component.operation();

        long end = System.currentTimeMillis();

        log.info("실행 시간 : {}", (end - start));

        return result;
    }
}
