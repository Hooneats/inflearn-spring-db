package org.example.spring.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    /**
     *  Service      --->    Repository
     * memberService      1) memberRepository
     *                    2) logRepository
     */

    /**
     * memberService : @Transactional - Off
     * memberRepository : @Transactional - ON
     * logRepository : @Transactional - ON
     */
    @Test
    void outerTxOff_success() {
        // given
        String username = "outerTxOff_success";

        // when
        memberService.joinV1_NON_Tx(username);

        //then : 모든 데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService : @Transactional - Off
     * memberRepository : @Transactional - ON
     * logRepository : @Transactional - ON Exception
     */
    @Test
    void outerTxOff_fail() {
        // given
        String username = "로그예외_outerTxOff_fail";

        // when
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV1_NON_Tx(username))
                .isInstanceOf(RuntimeException.class);

        //then : log 데이터는 롤백된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService : @Transactional - ON
     * memberRepository : @Transactional - OFF
     * logRepository : @Transactional - OFF
     */
    @Test
    void singleTx() {
        // given
        String username = "singleTx";

        // when
        memberService.joinV1_Single_Tx(username);

        //then : 모든 데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService : @Transactional - ON
     * memberRepository : @Transactional - ON
     * logRepository : @Transactional - ON
     */
    @Test
    void outerTxOn_success() {
        // given
        String username = "outerTxOn_success";

        // when
        memberService.joinV1_Tx(username);

        //then : 모든 데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService : @Transactional - ON
     * memberRepository : @Transactional - ON
     * logRepository : @Transactional - ON Exception -> 정상 흐름 복구 안시키는 경우
     */
    @Test
    void recoverException_fail() {
        // given
        String username = "로그예외_outerTxOn_fail";

        // when
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV1_Tx(username))
                .isInstanceOf(RuntimeException.class);

        //then : 모든 데이터는 롤백된다.
        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * TODO : log 남기는것은 실패해도 회원가입은 성공되기를 원하는 경우
     * memberService : @Transactional - ON
     * memberRepository : @Transactional - ON
     * logRepository : @Transactional - ON Exception -> 정상 흐름 복구 시킬려는 경우
     * TODO : ㄴ 이는 logRepository 에서 트랜젝션을 setRollbackOnly = true 로 설정하고 memberService 로 전달하기에 전체 롤백이 이루어진다.
     *  때문에 propagation 설정이 필요하다.
     */
    @Test
    void recoverException_catch_fail() {
        // given
        String username = "로그예외_recoverException_fail";

        // when
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);

        //then : log 데이터만 롤백된다.
        Assertions.assertTrue(memberRepository.find(username).isEmpty()); // 테스트 통과 실패
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * TODO : log 남기는것은 실패해도 회원가입은 성공되기를 원하는 경우
     * memberService : @Transactional - ON
     * memberRepository : @Transactional - ON
     * logRepository : @Transactional - ON(REQUIRES_NEW) Exception -> 정상 흐름 복구 시킬려는 경우
     * TODO : ㄴ 이는 logRepository 에서 트랜젝션을 setRollbackOnly = true 로 설정하고 memberService 로 전달하기에 전체 롤백이 이루어진다.
     *  때문에 logRepository 에 propagation 설정이 필요하다. (주의할점은 실시간 유저가 많은 서비스일경우 커넥션 갯수가 부족해질 수도 있다.)
     */
    @Test
    void recoverException_requiresNew_success() {
        // given
        String username = "로그예외_recoverException_success";

        // when
        memberService.joinV2_Log_Requires_New(username);

        //then : log 데이터만 롤백된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent()); // 테스트 통과 성공
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }
}