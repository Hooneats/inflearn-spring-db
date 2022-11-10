package org.example.aop.proxyVS;

import lombok.extern.slf4j.Slf4j;
import org.example.aop.member.MemberService;
import org.example.aop.member.MemberServiceImpl;
import org.example.aop.proxyVS.code.ProxyDIAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

// TODO 프록시 생성방법에 따라 DI 문제
@Slf4j
@Import(ProxyDIAspect.class)
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) //JDK 동적 프록시 MemberServiceImpl 을 DI 받을 수 없다.
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) //CGLIB 프록시
@SpringBootTest // 기본이 CGLIB 프록시
public class ProxyDiTest {
    /**
     * TODO
     *  JDK 동적 프록시는 MemberServiceImpl 을 DI 받을 수 없다.
     *  하지만 CGLIB 는 DI 받을 수 있다. 그렇다면 CGLIB 는 단점이 없는가?
     *  스프링에서 CGLIB는 구체 클래스를 상속받아 프록시를 만든다. 그로인한 문제가 있다.
     *    ㄴ 대상 클래스에 기본 생성자 필수 : 자바 문법상 대상을 상속받으면 생성자에서 부모 생성자를 호출안하면 기본으로 첫줄에 super() 가 들어가기에
     *    ㄴ 생성자 2번 호출 문제 : CGLIB 는 프록시를 생성할때 구체클래스 생성자(super()) 호출 && target 객체 생성할 때(target())
     *    ㄴ final 키워드 클해스, 메서드 사용 불가 : final 키워드가 클래스에 있으면 상속 불가능 , method 에 있으면 오버라이딩 불가능
     *   ==> 이러한 문제를 스프링은 어떻게 풀었을까?
     *      ㄴ 스프링 3.2 는 스프링 내부에 CGLIB 를 함께 패키징해 별도의 라이브러리 추가 불필요
     *      ㄴ 스프링 4.0 부터는 'objenesis' 라는 라이브러리를 새용해 기본 생성자 없이 객체 생성이 가능하다.
     *                          ㄴ (CGLIB 기본 생성자 필수) 와 (생성자 두번 호출) 문제 해결
     *   ====> 따라서 스프링 부트 2.0 부터는 스프링은 CGLIB 프록시 방식을 기본으로 채택
     *              ㄴ 기본 설정 -> spring.aop.proxy-target-class=true
     */

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class = {}", memberService.getClass());
        log.info("memberServiceImpl class = {}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
