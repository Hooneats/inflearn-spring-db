package org.example.aop.exam;

import lombok.extern.slf4j.Slf4j;
import org.example.aop.exam.aop.RetryAspect;
import org.example.aop.exam.aop.TraceAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@Import({TraceAspect.class, RetryAspect.class}) // Spring Bean 으로 등록
@SpringBootTest
public class ExamServiceTest {

    @Autowired
    ExamService examService;

    @Test
    public void test() throws Exception{
        for (int i = 0; i < 5; i++) {
            log.info("client request i = {}", i);
            examService.request("data" + i);
        }
    }
}