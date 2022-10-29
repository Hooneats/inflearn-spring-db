package org.example.proxy.jdkdynamic.proxy.cglib.common;

import lombok.extern.slf4j.Slf4j;

// 인터페이스가 없는 클래
@Slf4j
public class ConcreteService {

    public void call() {
        log.info("ConcreteService 호출");
    }


}
