package org.example.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

// TODO 지연 주입
@Slf4j
@Component
public class CallServiceV2 {

    //    private final ApplicationContext applicationContext; // 지연조회만 할뿐인데 ApplicationContext 사용은 거창하다

//    public CallServiceV2(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }
    private final ObjectProvider<CallServiceV2> callServiceV2ObjectProvider; // 빈을 사용할 때 조회하는데 특화되어있는 ObjectProvider 가 좋다.

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceV2ObjectProvider) {
        this.callServiceV2ObjectProvider = callServiceV2ObjectProvider;
    }

    public void external() {
        log.info("call external");
//        CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);
        CallServiceV2 callServiceV2 = callServiceV2ObjectProvider.getObject();
        callServiceV2.internal(); // 외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");
    }
}
