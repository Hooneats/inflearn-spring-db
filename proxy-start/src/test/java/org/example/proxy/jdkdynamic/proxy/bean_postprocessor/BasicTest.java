package org.example.proxy.jdkdynamic.proxy.bean_postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {
        // 스프링 컨테이너에 등록
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);

        // A 는 빈으로 등록된다.
        A a = applicationContext.getBean("beanA", A.class);
        a.helloA();

        // B 는 빈으로 등록되지 않았다.
//        B b = applicationContext.getBean(B.class); // NoSuchBeanDefinitionException 발생
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(B.class));
    }

    @Slf4j
    @Configuration
    static class BasicConfig {

        @Bean(name = "beanA")
        public A a() {
            return new A();
        }
    }


    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A 호출");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B 호출");
        }
    }
}
