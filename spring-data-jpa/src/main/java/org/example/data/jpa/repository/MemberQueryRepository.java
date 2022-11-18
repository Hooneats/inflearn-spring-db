package org.example.data.jpa.repository;

import lombok.RequiredArgsConstructor;
import org.example.data.jpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findALlMembers() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
