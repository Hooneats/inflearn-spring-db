package org.example.aop.proxyVS;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.aop.member.MemberService;
import org.example.aop.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        // 구체 클래스도 있고 인터페이스도 있는 MemberServiceImpl
        MemberServiceImpl memberService = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(memberService);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시 -)ProxyFactory 의 기본값이 false 이기도하다

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // 캐스팅 오류 --> 이유는 JDK 동적 프록시는 MemberService 인터페이스를 사용했기에
        Assertions.assertThatThrownBy(() -> {
                    MemberService castingMemberService = (MemberServiceImpl) memberServiceProxy;
                })
                .isInstanceOf(ClassCastException.class);
        /**
         * JDK 동적 프록시는 대상객체인 구체 클래스로 캐스팅 할 수 없다.
         */
    }

    @Test
    void CGLIBProxy() {
        // 구체 클래스도 있고 인터페이스도 있는 MemberServiceImpl
        MemberServiceImpl memberService = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(memberService);
        proxyFactory.setProxyTargetClass(true); // CGLIB 프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // 캐스팅 성공
        MemberServiceImpl castingMemberServiceImpl = (MemberServiceImpl) memberServiceProxy;
        /**
         * CGLIB 는 MemberServiceImpl 을 상속받아 만들기 때문에 당연히 타겟이 MemberServiceImpl 타입으로 가능하고 MemberService 의 부모인 interface 까지 캐스팅 가능
         */
    }
}
