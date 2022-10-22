package com.example.jdbc.transaction.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV3;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜젝션 - DataSource , TransactionManager 자동 등록
 * 테스트에서는 스프링이 빈을 관리할 수 있도록 @SpringBootTest 추가
 *
 * TODO 참고로 @DataJpaTest 는 Spring 의 프록시 기능을 사용 못해 @Transactional 를 사용하지 못한다.
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class MemberServiceV3_4Test {

    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";
    private static final String MEMBER_EX = "ex";

    @Autowired
    private MemberRepositoryV3 memberRepositoryV3;
    @Autowired
    private MemberServiceV3_3 memberServiceV3_3;

    @Test
    void AopCheck() {
        Assertions.assertThat(AopUtils.isAopProxy(memberServiceV3_3)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepositoryV3)).isFalse();
    }

    @TestConfiguration
    static class TestConfig {

        private final DataSource dataSource;

        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepositoryV3 memberRepositoryV3() {
            return new MemberRepositoryV3(dataSource);
        }

        @Bean
        MemberServiceV3_3 memberServiceV3_3() {
            return new MemberServiceV3_3(memberRepositoryV3());
        }
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
        memberServiceV3_3.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
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
                () -> memberServiceV3_3.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000)
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
