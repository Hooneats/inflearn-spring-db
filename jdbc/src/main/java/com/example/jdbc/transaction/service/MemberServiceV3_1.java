package com.example.jdbc.transaction.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜젝션 - 트랜젝션 매니저
 * JDBC : DataSourceTransactionManager
 * JPA : JpaTransactionManager
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    private final PlatformTransactionManager transactionManager;

    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜젝션 시작 : getTransaction 은 매개변수로 트랜젝션 속성(정의 definition)을 넣어줘야한다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 비즈니스 로직 시작
            businessLogic(fromId, toId, money);

            transactionManager.commit(status); // 성공시 커밋 : 커넥션 반납 및 트랜젝션 종료
        } catch (Exception e) {
            transactionManager.rollback(status); // 실패시 롤백
            throw new IllegalStateException(e);
        } // transactionManager 가 commit 또는 rollback 후에 알아서 release(con.autoCommit(true) AND con.close()) 해준다.
    }

    private void businessLogic(String fromId, String toId, int money) throws SQLException {
        // 비즈니스 로직 시작
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void releaseConnection(Connection connection) throws SQLException {
        // 커넥션 풀을 사용할 경우 위에서 설정한 setAutoCommit: false 또한 유지한채 반납되기에 다시 true 설정 해줘야한다.
        connection.setAutoCommit(true); // 커넥션 풀 고료
        connection.close();
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외를 임의로 발생시킴");
        }
    }
}
