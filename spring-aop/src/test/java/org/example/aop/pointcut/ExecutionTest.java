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
public class ExecutionTest {

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

    @Test
    @DisplayName("가장 정확하게 매칭 - execution")
    void exactMatch() {
        // execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?) --> ? 는 생략가능
        //public abstract java.lang.String org.example.aop.member.MemberService.hello(java.lang.String)
        pointcut.setExpression("execution(public String org.example.aop.member.MemberService.hello(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("가장 많이 생략한 매칭 - execution")
    void allMatch() {
        // execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?) --> ? 는 생략가능
        //public abstract java.lang.String org.example.aop.member.MemberService.hello(java.lang.String)
        pointcut.setExpression("execution(* *(..))"); // 반환타입 메서드이름(파라미터)
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름으로 매칭")
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))"); // 반환타입 메서드이름(파라미터)
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름으로 매칭 - 패턴사용")
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름으로 매칭 - 패턴사용")
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *ll*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름으로 매칭실패")
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nano(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("패키지 정확하게 매칭")
    void packageExactMatch1() {
        pointcut.setExpression("execution(* org.example.aop.member.MemberService.hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패키지 정확하게 매칭 - 타입에 패턴")
    void packageExactMatch2() {
        pointcut.setExpression("execution(* org.example.aop.member.*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패키지 정확하게 매칭실패 - .")
    void packageExactFalse() {
        pointcut.setExpression("execution(* org.example.aop.*.*(..))"); // 이렇게 하면 딱 aop 패키지에 맞아야한다.
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("패키지 서브 패키지까지 매칭1 - ..")
    void packageExactMatchSubPackage1() {
        pointcut.setExpression("execution(* org.example.aop.member..*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패키지 서브 패키지까지 매칭2 - ..")
    void packageExactMatchSubPackage2() {
        pointcut.setExpression("execution(* org.example.aop..*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타입 매칭1 - 구체클래스")
    void typeMatch() {
        pointcut.setExpression("execution(* org.example.aop.member.MemberServiceImpl.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타입 매칭2 - 부모타입(인터페이스)")
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* org.example.aop.member.MemberService.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("부모타입으로 매칭시 부모타입에 있는것만가능")
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* org.example.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();

        AspectJExpressionPointcut pointcut2 = new AspectJExpressionPointcut();
        pointcut2.setExpression("execution(* org.example.aop.member.MemberServiceImpl.*(..))");
        Assertions.assertThat(pointcut2.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 - String")
    void argsMatch() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 - Void")
    void argsMatchNoArgs() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *())");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("파라미터 매칭 - 하나만 허용, 모든 타입")
    void argsMatchStar() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(*))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 - 모두허용")
    void argsMatchAll() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 - 특정 조건") // String 타입으로 시작, 갯수 무관, 나머지 모든 타입허용
    void argsMatchComplex() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String, ..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 - 특정 조건") // String 타입으로 시작, 갯수 2개, 마지막은 모든 타입허용
    void argsMatchComplex2() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String, *))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
