package org.example.spring.app.callback.v5;

import org.example.spring.trace.callback.TraceCallback;
import org.example.spring.trace.callback.TraceTemplate;
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
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate traceTemplate;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace logTrace) {
        this.orderService = orderService;
        this.traceTemplate = new TraceTemplate(logTrace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {

        //TODO 템플릿 콜백 패턴
        return traceTemplate.execute(
                "OrderControllerV4.request()",
                new TraceCallback<>() {
                    @Override
                    public String call() {
                        orderService.orderItem(itemId);
                        return "ok";
                    }
                }
        );
    }
}
