package com.example.jdbc.transaction.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜젝션 - 탬플릿 콜백 패턴 TransactionTemplate
 *  ㄴ TransactionTemplate 안에서 트랜젝션 시작과 닫기 모두 다 해준다.
 * JDBC : DataSourceTransactionManager
 * JPA : JpaTransactionManager
 *
 * 반환값이 있을때 execute()
 * 반환값이 없을때 executeWithoutResult()
 */
@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate txTemplate;

    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        // TransactionTemplate 은 transactionManager 가 필요하다.
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        txTemplate.executeWithoutResult(transactionStatus -> {
            // 비즈니스 로직 시작
            try {
                businessLogic(fromId, toId, money);
            } catch (SQLException e) {
                // 참고로` 언체크(런타임) 에러가 발생하면 롤백 / 체크 예외가 발생하면 커밋한다.
                // 또한 추가적으로 람다는 체크 예외를 밖으로 던질 수 없기에 런타임 에러로 변경해 주었다.
                throw new IllegalStateException(e);
            }
        });
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
