package org.example.proxy.GOF.pureproxy.decorator.code.decorator;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.GOF.pureproxy.decorator.code.Component;

// TODO : 데코레이터 패턴
// TODO : Decorator 적용 전 = Client -------> RealComponent
// TODO : Decorator 적용 후 = Client -------> Decorator -------> RealComponent
@Slf4j
public class MessageDecorator implements Component {

    private final Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();
        String decoratedResult = "***** " + result + " *****";
        log.info("decorator 전 : {} ----> 후 : {}", result, decoratedResult);
        return decoratedResult;
    }
}
