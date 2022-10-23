package org.example.spring.trace;

import lombok.Getter;

import java.util.UUID;

/**
 * TODO : 정상 요청
 * [796bccd9] OrderController.request()
 * [796bccd9] |-->OrderService.orderItem()
 * [796bccd9] | |-->OrderRepository.save()
 * [796bccd9] | |<--OrderRepository.save() time=1004ms
 * [796bccd9] |<--OrderService.orderItem() time=1014ms
 * [796bccd9] OrderController.request() time=1016ms
 * TODO : 예외 발생
 * [b7119f27] OrderController.request()
 * [b7119f27] |-->OrderService.orderItem()
 * [b7119f27] | |-->OrderRepository.save()
 * [b7119f27] | |<X-OrderRepository.save() time=0ms
 * ex=java.lang.IllegalStateException: 예외 발생!
 * [b7119f27] |<X-OrderService.orderItem() time=10ms
 * ex=java.lang.IllegalStateException: 예외 발생!
 * [b7119f27] OrderController.request() time=11ms
 * ex=java.lang.IllegalStateException: 예외 발생!
 */
@Getter
public class TraceId {
    private String id;
    private int level;

    public TraceId() {
        this.id = createID();
        this.level = 0;
    }

    public TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }
}
