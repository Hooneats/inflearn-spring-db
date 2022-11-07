package org.example.proxy.jdkdynamic.proxy.bean_postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO : 빈 후처리기 BeanPostProcessor
//  사실상 컴포넌트 스캔의 대상이 되는 빈들은 중간에 조작할 방법이 없다(우리가 직접 등록하는것이 아니기에)
//   하지만 빈 후처리기를 사용하면 빈을 중간에 조작할 수 있기에, 빈 객체를 프록시로 교체하는 것도 가능해진다.

/**
 * 참고로 : @PostConstruct 는
 * 스프링이 CommonAnnotationBeanPostProcessor 라는 빈 후처리기를 자동으로 등록하는데
 * @PostConstruct 가 붙은 메서드를 호출하는  빈 후처리기이기도 하다.
 *  즉 스프링 스스로도 스프링 내부의 기능을 확장하기 위해 빈 후처리기를 사용한다는 것이다.
 */
@Slf4j
public class BeanPostProcessorTest {

    @Test
    void basicConfig() {
        // 스프링 컨테이너에 등록
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // A 를 기대하지만 실제는 B 이기에 B로 바꿔치기해야한다. -> beanA 이름으로 B 객체가 빈으로 등록되었다.
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();

        // 결국 바꿔치기 되어 A 는 빈으로 등록되지 않았다.
//        A b = applicationContext.getBean(A.class); // NoSuchBeanDefinitionException 발생
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }

    @Configuration
    static class BeanPostProcessorConfig {

        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean
        public AToBPostProcessor helloPostProcessor() {
            return new AToBPostProcessor();
        }
    }


    static class A {
        public void helloA() {
            log.info("hello A 호출");
        }
    }

    static class B {
        public void helloB() {
            log.info("hello B 호출");
        }
    }

    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            // TODO : 빈 초기화 @PostConstruct 끝나고 나서
            log.info("beanName = {}, bean = {}", beanName, bean);
            if (bean instanceof A) {
                // A 를 그냥 B로 바꾸자
                return new B();
            }
            return bean;
        }
    }

}
