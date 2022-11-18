package org.example.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.example.querydsl.entity.Member;
import org.example.querydsl.entity.Team;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService initMemberService;

    // @PostConstruct 와 @Transactional 을 분리하는것이 좋아 이렇게 쓴다.
    @PostConstruct
    public void init() {
        initMemberService.init();
    }


    @Component
    static class InitMemberService {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100; i++) {
                Team selectedTeam = i % 2 == 0 ? teamA : teamB;
                em.persist(new Member("member" + i, i, selectedTeam));
            }

        }
    }

}
