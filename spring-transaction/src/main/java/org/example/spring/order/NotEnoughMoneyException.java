package org.example.spring.order;

/**
 *  비즈니스 요구사항
 *  1. 정상 : 주문시 결제를 성공하면 주문 데이터를 저장하고 결제 상태를 '완료' 로 처리한다.
 *  2. 시스템 예외 : 주문시 내부에 복구 불가능한 예외가 발생하면 전체 데이터를 롤백한다.
 *  3. 비즈니스 예외 : 주문시 결제 잔고가 부족하면 주문 데이터를 저장하고, 결제 상태를 '대기' 로 처리한다.
 */
public class NotEnoughMoneyException extends Exception {

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
