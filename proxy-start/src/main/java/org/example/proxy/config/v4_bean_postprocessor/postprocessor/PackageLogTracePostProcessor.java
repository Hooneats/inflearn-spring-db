package org.example.proxy.config.v4_bean_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

// 특정 패키지에 있는것만
@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {
    private final String basePackage;
    private final Advisor advisor; // 어드바이저로 프록시 객체로 바꾸기 위해

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("param BeanName = {}, bean = {}", beanName, bean.getClass());

        // 프록시 대상 여부 체크
        // 프록시 적용 대상이 아니면 원본을 그대로 진행
        String packageName = bean.getClass().getPackageName();
        if (!packageName.startsWith(basePackage)) {
            // 적용대상 아니면
            return bean;
        }
        // 적용대상인 경우 프록시를 만들어서 반환
        ProxyFactory proxyFactory = new ProxyFactory(bean); // target 은 bean
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy();
        log.info("create proxy : target = {}, proxy = {}", bean.getClass(), proxy.getClass());
        return proxy;
    }
}
