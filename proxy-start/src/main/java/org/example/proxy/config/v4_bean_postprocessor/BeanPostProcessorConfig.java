package org.example.proxy.config.v4_bean_postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.example.proxy.config.AppV1Config;
import org.example.proxy.config.AppV2Config;
import org.example.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import org.example.proxy.config.v4_bean_postprocessor.postprocessor.PackageLogTracePostProcessor;
import org.example.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // v1, v2 , v3(v3 는 컴포넌트 대상이기에) 까지 전부 적용하기위해 편의상 여기서 @Import
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTracePostProcessor logTracePostProcessor(LogTrace logTrace) {
        return new PackageLogTracePostProcessor("org.example.proxy.app", getAdvisor(logTrace));
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        //advisor
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
    /**
     * JDK 동적 프록시 V1 , CGLIB V2/V3
     * create proxy : target = class org.example.proxy.app.v1.OrderServiceV1Impl, proxy = class com.sun.proxy.$Proxy52
     * create proxy : target = class org.example.proxy.app.v2.OrderRepositoryV2, proxy = class org.example.proxy.app.v2.OrderRepositoryV2$$EnhancerBySpringCGLIB$$1f477b1e
     * create proxy : target = class org.example.proxy.app.v3.OrderRepositoryV3, proxy = class org.example.proxy.app.v3.OrderRepositoryV3$$EnhancerBySpringCGLIB$$2d28c9a4
     */
}
