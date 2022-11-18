package org.example.querydsl.repository;

import org.example.querydsl.dto.MemberSearchCondition;
import org.example.querydsl.dto.MemberTeamDto;
import org.example.querydsl.entity.Member;
import org.example.querydsl.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void basicTest() throws Exception{
        // given
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberJpaRepository.findAll();
        assertThat(result1).containsExactly(member);
        List<Member> result1_1 = memberJpaRepository.findAll_Querydsl();
        assertThat(result1_1).containsExactly(member);

        List<Member> result2 = memberJpaRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);
        List<Member> result2_1 = memberJpaRepository.findByUsername_Querydsl("member1");
        assertThat(result2_1).containsExactly(member);
    }

    /**
     * 동적 쿼리와 성능 최적화 조회 - Builder 사용
     * */
    @Test
    public void searchTest() throws Exception{
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

//        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
        /** Where 절 파라미터 사용 추천 */
        List<MemberTeamDto> result = memberJpaRepository.search(condition);
        for (MemberTeamDto memberTeamDto : result) {
            System.out.println("memberTeamDto = " + memberTeamDto);
        }
        assertThat(result).extracting("username").containsExactly("member4");
    }



}