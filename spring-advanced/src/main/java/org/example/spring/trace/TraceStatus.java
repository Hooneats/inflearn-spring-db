package org.example.spring.trace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TraceStatus {
    private TraceId traceId;
    private Long startTimeMs; // 로그 시작시간
    private String message; // 메시지

}
