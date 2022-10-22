package org.example.spring.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private String username; // 정성, 예외, 잔고부족
    private String payStatus; // 대기 , 종료

}
