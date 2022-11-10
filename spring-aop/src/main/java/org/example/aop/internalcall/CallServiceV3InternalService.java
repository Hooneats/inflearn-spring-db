package org.example.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//TODO 구조변경
@Slf4j
@Component
public class CallServiceV3InternalService {

    public void internal() {
        log.info("call internal");
    }

}
