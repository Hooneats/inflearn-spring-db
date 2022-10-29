package org.example.proxy.jdkdynamic.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * TODO :
 *  동적 프록시 - reflection 사용
 *   그런데 reflection 기술은 런타임에 동작하기에 컴파일 시점에 오류를 잡지 못하고 런타임에 오류를 잡을 수 있다.
 *   때문에 리플렉션은 가급적이면 사용하지 않는것이 좋다.
 *   ㄴ리플렉션은 프레임워크 개발이나 또는 매우 일반적인 공통 처리가 필요할때 부분적으로 주의해서 사용해야 한다.
 */
@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        //공통로직1 시작
        log.info("start");
        String resultA = target.callA();
        log.info("result = {}", resultA);
        //공통로직2 시작
        log.info("start");
        String resultB = target.callB();
        log.info("result = {}", resultB);
        // ---> 공통로직은 똑같다. 이를 동적으로 합칠 수 있을까?
    }

    @Test
    void reflection1()
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //클래스 정보
        Class<?> classHello = Class.forName(
            "org.example.proxy.jdkdynamic.proxy.ReflectionTest$Hello");//내부 클래스는 $ 사용

        Hello target = new Hello();

        // callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }

    private void dynamicCall(Method method, Object target)
        throws InvocationTargetException, IllegalAccessException {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result = {}", result);
    }

    @Test
    void reflection2()
        throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        //클래스 정보
        Class<?> classHello = Class.forName(
            "org.example.proxy.jdkdynamic.proxy.ReflectionTest$Hello");//내부 클래스는 $ 사용

        Hello target = new Hello();

        // callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    static class Hello {

        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
