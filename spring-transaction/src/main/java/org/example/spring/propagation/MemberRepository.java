package org.example.spring.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.propagation.entity.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    @Transactional
    public void save_Tx(Member member) {
        log.info("member 저장");
        em.persist(member);
    }

    public void save_NON_Tx(Member member) {
        log.info("member 저장");
        em.persist(member);
    }

    public Optional<Member> find(String username) {
        return em.createQuery("select m from member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findAny();
    }
}
