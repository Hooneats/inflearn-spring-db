package org.example.spring.trace.logtrace.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.example.spring.trace.TraceId;
import org.example.spring.trace.TraceStatus;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>(); // thraceId 동기화, 이렇게 쓰면 사실 동시성 이슈 발생하는 코드이다.

    // 로그 시작
    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}",
                traceId.getId(),
                addSpace(START_PREFIX, traceId.getLevel()),
                message
        );
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // 트레이스 아이디 새로만들거나 직전에 만든게 있다면 레벨 증가
    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
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

        releaseTraceId();
    }

    // 첫번째 traceId 이면 파괴 그게 아니면 레벨 감소
    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            /**
             * 현 쓰레드가 사용한 쓰레드 로컬의 값을 삭제
             * TODO 꼭 해줘야하는 작업!
             */
            traceIdHolder.remove(); // destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId());
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
            sb.append((i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }
}
