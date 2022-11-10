package org.example.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

// TODO 구조변경 - 스프링에서 권장
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {

    private final CallServiceV3InternalService internalService;

    public void external() {
        log.info("call external");
        internalService.internal(); // 외부 메서드 호출
    }

}
