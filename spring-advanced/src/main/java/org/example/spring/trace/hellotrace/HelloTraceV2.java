package org.example.spring.trace.hellotrace;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.trace.TraceId;
import org.example.spring.trace.TraceStatus;
import org.springframework.stereotype.Component;

/**
 * TODO : 남은 문제
 * HTTP 요청을 구분하고 깊이를 표현하기 위해서 TraceId 동기화가 필요하다.
 * TraceId 의 동기화를 위해서 관련 메서드의 모든 파라미터를 수정해야 한다.
 * 만약 인터페이스가 있다면 인터페이스까지 모두 고쳐야 하는 상황이다.
 * 로그를 처음 시작할 때는 begin() 을 호출하고, 처음이 아닐때는 beginSync() 를 호출해야 한다.
 * 만약에 컨트롤러를 통해서 서비스를 호출하는 것이 아니라, 다른 곳에서 서비스를 처음으로 호출하는
 * 상황이라면 파리미터로 넘길 TraceId 가 없다.
 * TODO : HTTP 요청을 구분하고 깊이를 표현하기 위해서 TraceId 를 파라미터로 넘기는 것 말고 다른 대안은
 *        없을까?
 */
@Slf4j
@Component
public class HelloTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    // 로그 시작
    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}",
                traceId.getId(),
                addSpace(START_PREFIX, traceId.getLevel()),
                message
        );
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // V2 에 추가
    public TraceStatus beginSync(TraceId beforeTraceId,String message) {
        TraceId nextId = beforeTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}",
                nextId.getId(),
                addSpace(START_PREFIX, nextId.getLevel()),
                message
        );
        return new TraceStatus(nextId, startTimeMs, message);
    }

    // 로그 정상 종료
    public void end(TraceStatus status) {
        complete(status, null);
    }

    // 로그 예외로 종료
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs
            );
        } else {
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs,
                    e.toString()
            );
        }
    }

    // level=0
    // level=1      |-->
    // level=2      |   |-->

    // level=2 ex   |   |<X-
    // level=1 ex   |<X-
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }
}
