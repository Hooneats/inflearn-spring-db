package com.example.jdbc.transaction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepository;
import com.example.jdbc.repository.MemberRepositoryV4_1;
import com.example.jdbc.repository.MemberRepositoryV4_2;
import com.example.jdbc.repository.MemberRepositoryV5;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 */
@SpringBootTest
class MemberServiceV4Test {
    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";
    private static final String MEMBER_EX = "ex";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberServiceV4 memberServiceV4;

    @Test
    void AopCheck() {
        Assertions.assertThat(AopUtils.isAopProxy(memberServiceV4)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }

    @TestConfiguration
    static class TestConfig {

        private final DataSource dataSource;

        TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepository memberRepositoryV4_1() {
//            return new MemberRepositoryV4_1(dataSource);
//            return new MemberRepositoryV4_2(dataSource);
            return new MemberRepositoryV5(dataSource);
        }

        @Bean
        MemberServiceV4 memberServiceV3_3() {
            return new MemberServiceV4(memberRepositoryV4_1());
        }
    }

    @AfterEach
    void deleteUp() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @DisplayName("정상 이체")
    @Test
    void accountTransfer() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        //when
        System.out.println("Start Tx");
        memberServiceV4.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
        System.out.println("Ene Tx");

        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @DisplayName("이체중 예외 발생")
    @Test
    void accountTransferEx() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_EX, 10000);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        //when
        System.out.println("Start Tx");
        assertThatThrownBy(
            () -> memberServiceV4.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000)
        ).isInstanceOf(IllegalStateException.class);
        System.out.println("Ent Tx");

        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());

        /**
         * 비즈니스 로직상 예외 발생으로 rollback 실행되어
         * memberA , memberB 둘다 변경된 사항이 없어야한다.
         */
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }
}
