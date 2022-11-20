package org.example.spring.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.propagation.entity.Log;
import org.example.spring.propagation.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * propagation 기본은 REQUIRED
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    // TODO 정상 흐름 -> log , member 각각 트랜젝션 실행
    public void joinV1_Tx(String username) {
        Member member = new Member(username);
        Log logMessage = new Log((username));

        log.info("===> memberRepository 트랜젝션 호출 시작");
        memberRepository.save_Tx(member);
        log.info("===> memberRepository 트랜젝션 호출 종료");

        log.info("===> logRepository 트랜젝션 호출 시작");
        logRepository.save_Tx(logMessage);
        log.info("===> logRepository 트랜젝션 호출 종료");
    }

    // TODO 정상 흐름 -> log , member 각각 트랜젝션 실행
    public void joinV1_NON_Tx(String username) {
        Member member = new Member(username);
        Log logMessage = new Log((username));

        log.info("===> memberRepository 트랜젝션 호출 시작");
        memberRepository.save_Tx(member);
        log.info("===> memberRepository 트랜젝션 호출 종료");

        log.info("===> logRepository 트랜젝션 호출 시작");
        logRepository.save_Tx(logMessage);
        log.info("===> logRepository 트랜젝션 호출 종료");
    }

    @Transactional
    // TODO 정상 흐름 -> log , member 각각 트랜젝션 실행
    public void joinV1_Single_Tx(String username) {
        Member member = new Member(username);
        Log logMessage = new Log((username));

        log.info("===> memberRepository 트랜젝션 호출 시작");
        memberRepository.save_NON_Tx(member);
        log.info("===> memberRepository 트랜젝션 호출 종료");

        log.info("===> logRepository 트랜젝션 호출 시작");
        logRepository.save_NON_Tx(logMessage);
        log.info("===> logRepository 트랜젝션 호출 종료");
    }

        @Transactional
    // TODO 로그 예외 발생
    public void joinV2(String username) {
        Member member = new Member(username);
        Log logMessage = new Log((username));

        log.info("===> memberRepository 트랜젝션 호출 시작");
        memberRepository.save_Tx(member);
        log.info("===> memberRepository 트랜젝션 호출 종료");

        log.info("===> logRepository 트랜젝션 호출 시작");
        try {
            logRepository.save_Tx(logMessage);
        } catch (RuntimeException e) {
            // logRepository 에서 propagation = REQUIRES_NEW 로 신규 생성되도록 만들었어도,(트랜젝션을 새로만든거지 예외를 잡은게 아니기에)
            // logRepository 에서 발생시킨 에러는 트랜젝션과는 별개이기에 여기서 에러를 잡아 처리해야한다.
            log.info("log 저장에 실패했습니다. logMessage = {}", logMessage);
            log.info("정상 흐름 반환");
        }
        log.info("===> logRepository 트랜젝션 호출 종료");
    }
}
