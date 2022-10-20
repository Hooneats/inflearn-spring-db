package com.example.jdbc.transaction.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;

import static com.example.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜젝션 - 트랜젝션 탬플릿
 *  * JDBC : DataSourceTransactionManager
 *  * JPA : JpaTransactionManager
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class MemberServiceV3_2Test {

    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";
    private static final String MEMBER_EX = "ex";

    private MemberRepositoryV3 memberRepositoryV3;
    private MemberServiceV3_2 memberServiceV3_2;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV3 = new MemberRepositoryV3(dataSource);
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        memberServiceV3_2 = new MemberServiceV3_2(transactionManager, memberRepositoryV3);
    }

    @AfterEach
    void deleteUp() throws SQLException {
        memberRepositoryV3.delete(MEMBER_A);
        memberRepositoryV3.delete(MEMBER_B);
        memberRepositoryV3.delete(MEMBER_EX);
    }

    @DisplayName("정상 이체")
    @Test
    void accountTransfer() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);

        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberB);
        //when
        System.out.println("Start Tx");
        memberServiceV3_2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
        System.out.println("Ene Tx");

        //then
        Member findMemberA = memberRepositoryV3.findById(memberA.getMemberId());
        Member findMemberB = memberRepositoryV3.findById(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @DisplayName("이체중 예외 발생")
    @Test
    void accountTransferEx() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_EX, 10000);

        memberRepositoryV3.save(memberA);
        memberRepositoryV3.save(memberB);
        //when
        System.out.println("Start Tx");
        assertThatThrownBy(
                () -> memberServiceV3_2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000)
        ).isInstanceOf(IllegalStateException.class);
        System.out.println("Ent Tx");

        //then
        Member findMemberA = memberRepositoryV3.findById(memberA.getMemberId());
        Member findMemberB = memberRepositoryV3.findById(memberB.getMemberId());

        /**
         * 비즈니스 로직상 예외 발생으로 rollback 실행되어
         * memberA , memberB 둘다 변경된 사항이 없어야한다.
         */
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }
}
