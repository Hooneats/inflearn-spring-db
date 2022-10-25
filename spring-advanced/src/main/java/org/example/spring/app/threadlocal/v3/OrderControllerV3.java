package org.example.spring.app.threadlocal.v3;

import lombok.RequiredArgsConstructor;
import org.example.spring.trace.TraceStatus;
import org.example.spring.trace.logtrace.threadlocal.LogTrace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 동시성 문제 발생 FieldLogTrace.traceIdHolder
 *  ㄴ FieldLogTrace 가 싱글톤으로 존재하는데 싱글톤의 멤버변수인 traceIdHolder 를 사용하고 있기에 동시성 문제 발생
 *  ㄴ 동시성 문제는 지역변수에서는 발생하지 않는다. 지역변수는 쓰레드마다 각각 다른 메모리 영역을 할당받기 떄문이다.
 *  ㄴ 그렇다면 싱글톤 + 멤버변수를 사용하려면 어떻게 해야하는가? 답은 Thread Local 에 있다.
 *      ㄴ 쓰레드 로컬이란 해당 쓰레드만 접근할 수 있는 특별한 저장소를 말한다. ( java .lang.ThreadLocal )
 *          ㄴ 쓰레드 로컬은 사용하는 쓰레드가 끝날때 remove 를 해줘야한다. 그렇지 않으면 WAS 처럼 쓰레드 풀을 사용하는 서비스인경우
 *              큰 문제가 생길 수 있다.(제거를 하지 않으면 쓰레드 풀에 반납 후 데이터는 남아있기에 다른 사용자와 공유하게 되는 꼴이다.)
 *  TODO ==> 적용 전 Bean 을 FieldLogTrace 사용
 *           적용 후 Bean 을 ThreadLocalLogTrace 사용
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;
    private final LogTrace trace; // 동시성 문제 발생

    @GetMapping("/v3/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; // 서비스상 예외를 꼭 다시 던져주어야 한다. - 로그는 서비스 흐름에 영향을 주면 안되기에 로그로인해 정상흐름이 되면 안된다.
        }

        return "ok";
    }
}
