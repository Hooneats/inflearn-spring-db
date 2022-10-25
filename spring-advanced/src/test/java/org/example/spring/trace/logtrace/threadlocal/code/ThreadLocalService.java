package org.example.spring.trace.logtrace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {

    /**
     * 쓰레드 로컬 사용시 주의사항으로는 해당 쓰레드가 쓰레드 로컬을 모두 사용하고 나면
     * ThreadLocal.remove() 를 호출해서 쓰레드 로컬에 저장된 값을 제거해주어야한다. --> 메모리 누수 발생할 수 있기에
     */
    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String login(String name) {
        log.info("저장 name {} -> nameStore {}", name, nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore {}", nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
