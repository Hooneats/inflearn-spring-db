package org.example.aop.exam;

import lombok.extern.slf4j.Slf4j;
import org.example.aop.exam.annotation.Retry;
import org.example.aop.exam.annotation.Trace;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExamRepository {

    private static int seq = 0;

    /**
     * 5번에 1번 실패하는 요청
     */
    @Trace
    @Retry(4) // 3 번 재시도
    public String save(String itemId) {
        seq++;
        if ((seq % 5) == 0) {
            log.error("예외 발생");
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
