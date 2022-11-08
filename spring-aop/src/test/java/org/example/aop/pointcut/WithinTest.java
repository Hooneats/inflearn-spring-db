package org.example.aop.pointcut;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.aop.member.MemberService;
import org.example.aop.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class WithinTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        this.helloMethod = MemberService.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        //public abstract java.lang.String org.example.aop.member.MemberService.hello(java.lang.String)
        log.info("helloMethod = {}", helloMethod);
    }

    /**
     * 주의할점은 표현식에 부모타입을 지정하면 안된다! 이부분이 execution 과 차이가 난다.
     * execution 을 많이 쓰고 within 은 잘 안쓴다.
     */

    @Test
    @DisplayName("타입 매칭")
    void WithinExact() {
        //public abstract java.lang.String org.example.aop.member.MemberService.hello(java.lang.String)
        pointcut.setExpression("within(org.example.aop.member.MemberServiceImpl)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타입 매칭 - 패턴")
    void WithinExactStar() {
        pointcut.setExpression("within(org.example.aop.member.*Service*)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타입 매칭 - 패턴")
    void WithinSubPackage() {
        pointcut.setExpression("within(org.example.aop..*)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타입 매칭 - 인터페이스 안된다.")
    void WithinSuperTypeFalse() {
        pointcut.setExpression("within(org.example.aop.member.MemberService)");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("execution 매칭 - 인터페이스 된다.")
    void executionSuperTypeTrue() {
        pointcut.setExpression("execution(* org.example.aop.member.MemberService.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
