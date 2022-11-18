package org.example.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.querydsl.dto.MemberSearchCondition;
import org.example.querydsl.dto.MemberTeamDto;
import org.example.querydsl.dto.QMemberTeamDto;
import org.example.querydsl.entity.QTeam;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.querydsl.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    // SpringBoot시작지점에 @Bean 으로 등록해놨기에 new 로 안쓰고 이렇게 써도된다.
    public MemberQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    /**
     * 추천
     * 동적 쿼리와 성능 최적화 조회 - Where절 파라미터 사용
     */
    public List<MemberTeamDto> search(MemberSearchCondition condition) {

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        QTeam.team.id.as("teamId"),
                        QTeam.team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, QTeam.team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? QTeam.team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

}
