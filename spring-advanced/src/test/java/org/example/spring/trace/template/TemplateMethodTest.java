package org.example.spring.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.trace.template.code.AbstractTemplate;
import org.example.spring.trace.template.code.SubClassLogin1;
import org.example.spring.trace.template.code.SubClassLogin2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 변하는 부분 - > 비즈니스 로직
 * 변하지 않는부분 - > 시간을 측정하는 부분
 *
 * TODO : 즉 템플릿 메서드 패턴은 = 변하는 부분과 변하지 않는부분을 분리할 수 있게 해준다.
 */
@Slf4j
class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        login1();
        login2();
    }

    private void login1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행"); // 변하는 부분
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("result Time = {}", resultTime);
    }

    private void login2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직2 실행"); // 변하는 부분
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("result Time = {}", resultTime);
    }

    @DisplayName("템플릿 메서드 패턴 적용")
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogin1();
        template1.execute();
        AbstractTemplate template2 = new SubClassLogin2();
        template2.execute();
    }

    @DisplayName("템플릿 메서드 패턴 적용 (익명 내부 클래스로)")
    @Test
    void templateMethodV2() {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직 1 실행");
            }
        };
        log.info("클래스 이름 1 = {}", template1.getClass()); // 결과 : TemplateMethodTest$1 => 현재클래스&(자바가 만든 숫자)
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직 2 실행");

            }
        };
        log.info("클래스 이름 2 = {}", template1.getClass());
        template2.execute();
    }
}