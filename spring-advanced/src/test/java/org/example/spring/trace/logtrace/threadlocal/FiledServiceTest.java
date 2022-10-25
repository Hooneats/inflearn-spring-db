package org.example.spring.trace.logtrace.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.trace.logtrace.threadlocal.code.FieldService;
import org.junit.jupiter.api.Test;

@Slf4j
public class FiledServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> fieldService.login("userA");
        Runnable userB = () -> fieldService.login("userB");

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2000); // 쓰레드 휴식을 했기에 동시성 문제 발생 x
        sleep(100); // 쓰레드 휴식을 굉장히 작게 했기에 동시성 문제 발생 O
        threadB.start();

        sleep(3000); // 메인 쓰레드 종료 대기
        log.info("main exit");

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
