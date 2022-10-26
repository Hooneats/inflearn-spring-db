package org.example.spring.trace.template.code;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO : 템플릿 메서드 패턴은 = 변하는 부분과 변하지 않는부분을 분리할 수 있게 해준다.
 */
@Slf4j
public abstract class AbstractTemplate {

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call(); // 변하는 부분
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("result Time = {}", resultTime);
    }

    protected abstract void call();

}
