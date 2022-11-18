package org.example.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.querydsl.dto.MemberSearchCondition;
import org.example.querydsl.dto.MemberTeamDto;
import org.example.querydsl.dto.QMemberTeamDto;
import org.example.querydsl.entity.Member;
import org.example.querydsl.entity.QTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static org.example.querydsl.entity.QMember.member;
import static org.example.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

/**
 * 주의!!!!
 * 중요한 주의사항은 실제 사용할 Repository 인 MemberRepository 와 이름을 같게하여 그 뒤에 Impl 을 붙인다.
 * MemberRepositoryImpl.java 로 Querydsl 부분인 MemberRepositoryCustom 인터페이스를 받아 구현한뒤 MemberRepository 에 상속한다.
 *
 * */
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // SpringBoot시작지점에 @Bean 으로 등록해놨기에 new 로 안쓰고 이렇게 써도된다.
    public MemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    /**
     * 추천
     * 동적 쿼리와 성능 최적화 조회 - Where절 파라미터 사용
     */
    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {

        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 직접 토탈 카운트를 날린다 -> 컨텐트 쿼리는 복잡하지만 카운트 쿼리는 심플할 경우 이렇게 따로 하는게 좀더 성능상 좋다.
//        JPAQuery<Member> countQuery = queryFactory //-> 카운트쿼리 변경 이렇게 사용은 deprecated
//                .select(member)
//                .from(member)
//                .leftJoin(member.team, QTeam.team)
//                .where(
//                        usernameEq(condition.getUsername()),
//                        teamNameEq(condition.getTeamName()),
//                        ageGoe(condition.getAgeGoe()),
//                        ageLoe(condition.getAgeLoe())
//                );
        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );

        /**
         * 카운트 쿼리 최적화!!!!!!!!
         * */

//        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
        // 이렇게 만들면 알아서 토탈 카운트가 정말 날려 필요한경우 알아서 쿼리 날려서 가져옴
//        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount); // deprecated
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
//        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

}
