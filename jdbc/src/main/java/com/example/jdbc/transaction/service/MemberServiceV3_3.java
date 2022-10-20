package com.example.jdbc.transaction.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜젝션 - @Transactional AOP (프록시 사용)
 * @Transactional 은 클래스에 붙이면 모든 public 메서드에 적용됨
 *
 * TODO
 *    [PlatformTransactionManager]
 *          트랜젝션 매니저 -----(1.커넥션생성 2.autoCommit(false) 해 커넥션 보냄)------> 트랜젝션 동기화 매니저 (커넥션 관리)
 *              |                                                                           |(커넥션 획득 및 트랜젝션 커넥션 동기화)
 *          서비스 로직 시작(트랜젝션 시작) -----------------------> 비즈니스 로직 ----------------> 데이터 접근 로직
 *              [AOP 프록시]                                    [서비스]                        [레포지토리]
 */
@Slf4j
public class MemberServiceV3_3 {

    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        businessLogic(fromId, toId, money);
    }

    private void businessLogic(String fromId, String toId, int money) throws SQLException {
        // 비즈니스 로직 시작
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외를 임의로 발생시킴");
        }
    }
}
