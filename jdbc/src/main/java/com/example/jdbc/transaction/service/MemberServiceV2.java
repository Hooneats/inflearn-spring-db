package com.example.jdbc.transaction.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 클라이언트 요청 하나당 ---- [Connection] ---- DB ----> Session
 * (Session 이 Transaction 의 범위이다)
 *
 * 따라서 트랜젝션은 Connection 이 유지되어야 트랜젝션 또한 유지된다.
 * (autoCommit 을 false 로 변경해 Connection 을 유지시킨다.)
 *
 * 트랜젝션 - Connection 을 파라미터로 연동, 커넥션 풀을 고려한 종료.
 * (커넥션 풀을 고려해 autoCommit 을 다시 true 로 변경한 뒤 Connection 을 반납해 줘야 한다.)
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;

    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false); // 트랜젝션 시작

            // 비즈니스 로직 시작
            businessLogic(connection, fromId, toId, money);

            connection.commit(); // 성공시 커밋 : 커넥션 반납 및 트랜젝션 종료
        } catch (Exception e) {
            connection.rollback(); // 실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            if (connection != null) {
                try {
                    // 커넥션 풀을 사용할 경우 위에서 설정한 setAutoCommit: false 또한 유지한채 반납되기에 다시 true 설정 해줘야한다.
                    releaseConnection(connection);
                } catch (Exception e) {
                    log.info("error", e);
                }
            }
        }
    }

    private void businessLogic(Connection connection, String fromId, String toId, int money) throws SQLException {
        // 비즈니스 로직 시작
        Member fromMember = memberRepository.findById(connection, fromId);
        Member toMember = memberRepository.findById(connection, toId);

        memberRepository.update(connection, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(connection, toId, toMember.getMoney() + money);
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
