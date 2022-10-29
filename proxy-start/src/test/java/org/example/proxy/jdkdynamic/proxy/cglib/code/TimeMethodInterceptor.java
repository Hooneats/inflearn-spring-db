package org.example.proxy.jdkdynamic.proxy.cglib.code;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * 스프링에서 사용하는 오픈소스 CGLIB
 * 매개변수
 * 1. obj : CGLIB 가 적용된 객체
 * 2. method : 호출될 메서드
 * 3. args : 전달될 인수
 * 4. proxy : 메서드 호출에 사용
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    // TODO : 항상 프록시는 내부호출할 대상 클래스 target 이 필요
    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable {
        log.info("TimeProxy 실행");
        long start = System.currentTimeMillis();

//        Object result = method.invoke(target, args);
        // 위에보다 CGLIB 에서 제공하는 methodProxy 로 invoke 하는게 빠르다고 한다.
        Object result = methodProxy.invoke(target, args);

        long end = System.currentTimeMillis();
        log.info("실행 시간 = {}", end - start);
        return result;
    }
}
