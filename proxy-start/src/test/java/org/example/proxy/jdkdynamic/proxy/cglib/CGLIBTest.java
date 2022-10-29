package org.example.proxy.jdkdynamic.proxy.cglib;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.jdkdynamic.proxy.cglib.code.TimeMethodInterceptor;
import org.example.proxy.jdkdynamic.proxy.cglib.common.ConcreteService;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CGLIBTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        // TODO :  CGLIB 는 Enhancer 를 사용한다.
        // TODO : CGLIB 는 상속을 통해 프록시를 생성하기에 부모 클래스의 기본생성자가 있어야한다.
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class);
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create(); // setSuperclass 로 상속받았기에

        proxy.call();
        log.info("target class = {}", target.getClass());
        log.info("proxy class = {}", proxy.getClass());

    }
}
