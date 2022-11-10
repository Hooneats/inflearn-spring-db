package org.example.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.example.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;

    @Test
    void external() {
        log.info("class = {}", callServiceV0.getClass());
        //external 호출전에는 aop 가 적용되어 로그 찍힘 그러나 internal 할때는 aop 로그가 찍히지 않음,
        // 즉 메서드 안에 다른 내부메서드 실행시 적용안된다.
        // TODO : 이는 external 안에서 internal 호출은 프록시가 하는 것이 아닌 타겟 인스턴스가 this.internal()을 호출하는 작용이기에 이렇다.
        //  참고로 AspectJ 를 사용하면 프록시를 통하는 것이 아니라 해당 코드에 직접 AOP 적용 코드가 붙어 있기 떄문에 내부 포울과 무관하게 AOP 적용할 수 있다.
        //      그러나 이는 로드 타임 위빙 등을 사용해야하는데, 설정이 복잡하고 JVM 옵션을 주어야 하는 부담이 있다.
        //      그리고 스프링 프록시 방식의 AOP 에서 내부 호출에 대응할 수 있는 대안들이 있기에 AspectJ 를 직접 사용하지는 않는다.
        //          ㄴ 방법1 : 자기 자신을 DI 주입해 this.internal() 이 아닌 자신의 DI.internal() 실행되도록 하는 것이다.
        //              *단 이떄는 생성자 주입을 사용하면 자기자신을 생성할때 순환참조가 발생해 문제가 생긴다.
        //               * 그러나 스프링 부트 2.6 릴리즈 부터는 순환참조를 기본적으로 금지하여 setter주입 ,필드주입 모두 순환참조 발생 아래와 같은 설정 필요
        //                  * spring.main.allow-circular-references=true
        //
        callServiceV0.external();
    }

    @Test
    void internal() {
        callServiceV0.internal();
    }
}