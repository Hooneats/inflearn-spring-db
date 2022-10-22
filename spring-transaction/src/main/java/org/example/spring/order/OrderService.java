package org.example.spring.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // Jpa 는 트랜젝션 커밋 시점에 Order 데이터를 DB 에 반영한다.
    @Transactional
    public void order(Order order) throws NotEnoughMoneyException {
        log.info("order 호출");
        orderRepository.save(order);

        log.info("결제 프로세스 진입");
        if (order.getUsername().equals("예외")) {
            log.info("시스템 예외 발생, 언체크 예외 롤백");
            throw new RuntimeException();
        } else if (order.getUsername().equals("잔고부족")) {
            log.info("잔고 부족 비즈니스 예외 발생, 체크 예외 커밋 기대");
            order.setPayStatus("대기");
            throw new NotEnoughMoneyException("잔고가 부족 합니다.");
        } else {
            // 정상 승인
            log.info("정상 승인, 커밋");
            order.setPayStatus("완료");
        }

        log.info("결제 프로세스 완료");
    }
}
