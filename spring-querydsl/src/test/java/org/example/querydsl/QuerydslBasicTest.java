package org.example.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.example.querydsl.dto.MemberDto;
import org.example.querydsl.dto.QMemberDto;
import org.example.querydsl.dto.UserDto;
import org.example.querydsl.entity.Member;
import org.example.querydsl.entity.QMember;
import org.example.querydsl.entity.QTeam;
import org.example.querydsl.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.example.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    // JPAQueryFactory 는 멤버변수로 빼도 동시성 문제에 안전하다
    // 애초에 매개변수로받는 EntityManager 가 동시성에 안전하도록 설계되었기 때문에
    // 그래서 필드로 뺴서 쓰는 것을 권장한다.
    JPAQueryFactory queryFactory;


    @BeforeEach
    public void before() throws Exception {
        queryFactory = new JPAQueryFactory(em);
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
    }

    /**
     * jpql
     * */
    @Test
    public void startJPQL() throws Exception{
        // given
        // member1을 찾아라
        Member findMember = em.createQuery("select m from Member m " +
                        "where m.username=:username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 기본
     * */
    @Test
    public void startQuerydsl() throws Exception{
        // given
        // EntityManager 를 생성자에 넘겨주며 JPAQueryFactory 를 만들어야함
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        queryFactory = new JPAQueryFactory(em);
        // 별칭(m) 으로 구분하는것인데 이거 말고 다른것을 주로 쓴다. 일단 알고는 있기
//        QMember m = new QMember("m");
//        QMember m = QMember.member;

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    /**
     * 검색 search
     * */
    @Test
    public void search() throws Exception{
        // given
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();
        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * and 는 이런식으로 predicate 를 활용하는 것이 좋다.
     * */
    @Test
    public void searchAndParam() throws Exception{
        // given
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();
        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    /**
     * 기본적으로 제공하는 쿼리들
     * */
    @Test
    public void resultFetch() throws Exception{
        // given
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        // fetchFirst 는 limit(1).fetchOne() 과 같다
        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        // fetchResults 는 페이징처리에 사용 그러나 성능이 중요한 경우 fetchResults 를 안 쓰는게 좋다.
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();
        results.getTotal();
        List<Member> content = results.getResults();

        // fetchCount 는 count 쿼리로 변경
        long total = queryFactory
                .selectFrom(member)
                .fetchCount();
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 오름차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     * */
    @Test
    public void sort() throws Exception{
        // given
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();

    }

    /**
     * 페이징
     * */
    @Test
    public void paging1() throws Exception{
        // given
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);

    }
    /**
     * fetchResults 를 이용한 페이징
     * 이 조건은 content 쿼리는 복잡한데 count 쿼리는 단순하게 짤 수 있는 것이면
     * 성능상 이것을 안쓰고 따로 쓰는 것이 좋다.
     * */
    @Test
    public void paging2() throws Exception{
        // given
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    /**
     * 집합
     * select 에서 원하는 것들을 아래처럼 찍을경우 Tuple 로 조회하게 된다.
     * 실무에서는 tuple 을 많이 쓰지는 않고 DTO로 뽑아 많이 쓴다. 이방법은 나중에.
     * */
    @Test
    public void aggregation() throws Exception{
        // given
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }
    
    /**
     * 집합 group by groupBy
     * 팀의 이름과 각 팀의 평균 연령을 구해라
     *
     * groupBy() having() 도 가능
     * 예시로는
     * .groupBy(item.price)
     * .having(item.price.gt(1000))
     *  …
     * */
    @Test
    public void group() throws Exception{
        // given
        List<Tuple> result = queryFactory
                .select(
                        QTeam.team.name,
                        member.age.avg()
                )
                .from(member)
                .join(member.team, QTeam.team)
                .groupBy(QTeam.team.name)
                .fetch();
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(QTeam.team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15); // (10+20)/2
        assertThat(teamB.get(QTeam.team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35); // (30+40)/2
    }

    /**
     * 조인 - 기본 조인 기본조인
     * 팀 A 에 소속된 모든 회원
     * */
    @Test
    public void join() throws Exception{
        // given
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, QTeam.team)
                .where(QTeam.team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }
    /**
     * 조인 - 연관 관계가 없어도 조인가능
     * theta join  (cross join)
     * 회원 이름이 팀이름과 같은 회원 조회 (팀에 소속된 것은 아니여서 연관 관계가 없다.)
     * 위와 중요하게 다른것은 .from() 에서 위에는 연관관계가 있는것을 나열했는데
     * 이것은 그냥 나열만 한다.
     * 하지만 이런 theta join 은 외부 조인이 불가능하다. -> 그러나 최신에선 조인 on 을 사용하면 외부 조인도 가능
     * */
    @Test
    public void theta_join() throws Exception{
        // given
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, QTeam.team)
                .where(member.username.eq(QTeam.team.name))
                .fetch();
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA 인 팀만 조인, 회원은 모두 조회
     * JPQL : select m, t from Member m left join m.team t on t.name = "teamA"
     * */
    @Test
    public void join_in_filtering() throws Exception{
        // given
        List<Tuple> result = queryFactory
                .select(member, QTeam.team)
                .from(member)
                .leftJoin(member.team, QTeam.team)
                .on(QTeam.team.name.eq("teamA"))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 연관관계가 없는 엔티티 외부 조인 on 사용
     * 회원 이름이 팀 이름과 같은 대상 외부 조인
     * */
    @Test
    public void join_on_no_relation() throws Exception{
        // given
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, QTeam.team)
                .from(member)
                .leftJoin(QTeam.team)
                .on(member.username.eq(QTeam.team.name))
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 조인 - 페치 조인 사용
     * EntityManagerFactory.getPersistenceUnitUtil 로 이미 로딩된 엔티티인지 아니면 초기화가 안된 엔티티인지 확인가능
     */
    @PersistenceUnit
    EntityManagerFactory emf;
    @Test
    public void fetchJoinNo() throws Exception{
        // given
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }
    @Test
    public void fetchJoinUse() throws Exception{
        // given
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, QTeam.team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isTrue();
    }

    /**
     * 서브 쿼리
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery() throws Exception {

        // 서브 쿼리와 메인 쿼리가 겹치지 않게 따로 만들어주자
        QMember memberSub = new QMember("memberSub");

        // given
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();
        assertThat(result)
                .extracting("age")
                .containsExactly(40);
    }
    /**
     * 서브 쿼리
     * 나이가 평균 이상인 회원
     */
    @Test
    public void subQueryGoe() throws Exception {

        // 서브 쿼리와 메인 쿼리가 겹치지 않게 따로 만들어주자
        QMember memberSub = new QMember("memberSub");

        // given
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();
        assertThat(result)
                .extracting("age")
                .containsExactly(30, 40);
    }
    /**
     * 서브 쿼리
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQueryIn() throws Exception {

        // 서브 쿼리와 메인 쿼리가 겹치지 않게 따로 만들어주자
        QMember memberSub = new QMember("memberSub");

        // given
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(20, 30, 40);
    }
    /**
     * 서브 쿼리
     * */
    @Test
    public void selectSubQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");
        // given
        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * case 문 - 단순 조건 -이렇게 디비에서 하기보다는 앱내에서처리
     * */
    @Test
    public void basicCase() throws Exception{
        // given
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        for (String age : result) {
            System.out.println("age = " + age);
        }
    }
    /**
     * case 문 - 복잡한 조건 new CaseBuilder() 사용 -이렇게 디비에서 하기보다는 앱내에서처리
     * */
    @Test
    public void complexCase() throws Exception{
        // given
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        for (String age : result) {
            System.out.println("age = " + age);
        }
    }

    /**
     * 상수가 필요한 경우
     * Expressions 사용
     * */
    @Test
    public void constant() throws Exception{
        // given
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }
    /**
     * 문자 더하기가 필요한 경우
     * stringValue 사용
     * 이 방법은 ENUM 처리할 때도 stringValue 를 사용해서 처리 !!!
     * */
    @Test
    public void concat() throws Exception{
        // given
        // {username}_{age}
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
    
    /**
     * 중급 문법
     * */
    
    /**
     * 프로젝션 이란 select 해온 대상을 지정하는것
     * 프로젝션 대상이 하나
     * */
    @Test
    public void simpleProjection() throws Exception{
        // given
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
        List<Member> result2 = queryFactory
                .select(member)
                .from(member)
                .fetch();
        for (Member s : result2) {
            System.out.println("s = " + s);
        }
    }

    /**
     * 프로젝션 대상이 둘이상인경우
     * 프로젝션 튜플로
     * 튜플은 com.querydsl.core 소속이므로 DB 관련 기술이기에
     * Repository 내에서만 쓰는 것이 좋다.
     * 바깥 계층으로 나갈때는 DTO 로 변환해서 나가는 것을 권장한다.!
     * Service 나 Controller 까지 넘어가 쓰면
     * 좋은 설계가 아니다!
     * */
    @Test
    public void tupleProjection() throws Exception{
        // given
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();
        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    /**
     * 프로젝션 DTO 로 (자주쓰이며 중요)
     * */

    /**
     * 순수 JPA 에서 DTO로 조회
     * JPQL 에서 제공하는 new Operation 문법
     * 단점 : 코드가 지저분, 생성자 방식만 지원
    * */
    @Test
    public void findDtoByJPQL() throws Exception{
        // given
        List<MemberDto> result = em.createQuery("select new org.example.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    /**
     * QueryDSL 에서 DTO 로 조회
     * 1. Projections.bean 프로퍼티 접근 방법 - Setter -> 이떄 기본생성자가 있어야한다.(public 레벨의)
     * 2. Projections.fields 필드 직접 접근 방법 - 필드에 바로주입 -> getter , setter 없어도 된다.
     * 3. Projections.constructor 생성자 사용 방법 - 생성자 -> 매개변수의 순서와 타입이 맞아야한다.
     * 생성자 방식은 1번 2번과 다르게 매겨변수의 이름이 달라도 된다(생성자방식이라).
     * */
    @Test
    public void findDtoBySetter() throws Exception{
        // given
        List<MemberDto> result = queryFactory
                .select(Projections.bean(
                        MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    @Test
    public void findDtoByField() throws Exception{
        // given
        List<MemberDto> result = queryFactory
                .select(Projections.fields(
                        MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    @Test
    public void findDtoByConstructor() throws Exception{
        // given
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(
                        MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    /**
     * 필드주입과 Setter 주입의 경우
     * 필드명이 맞지 않을 경우 별칭을 준다.
     * 또한 서브쿼리를 사용해서 값을 넣어줄 수도 있다.
     * */
    @Test
    public void findUserDto() throws Exception{
        QMember memberSub = new QMember("memberSub");
        // given
        List<UserDto> result = queryFactory
                .select(Projections.fields(
                        UserDto.class,
                        member.username.as("name"), // 바로 별칭 넣기 -> 이또한 ExpressionUtils.as(member.username,name) 가능하다.

                        ExpressionUtils.as( // 서브쿼리에 별칭넣기
                                JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub),"age")

                ))
                .from(member)
                .fetch();
        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    /**
     * 프로젝션 @QueryProjection
     * DTO 생성자에 @QueryProjection 를 붙인뒤 똑같이 gradle compileQuerydsl 해주면
     * DTO 또한 Q 파일로 생성된다.
     * - 위의 생성자방식의 단점은 매개변수가 잘못되었을경우 컴파일 오류를 못잡고 런타임오류가 나지만 이방식은 컴파일 오류로 잡을수 있다.
     * - 하지만 이방식의 단점은 Q 파일을 생성해야하고, DTO 가 QueryDsl 에 의존성을 갖게되는 단점이있다...
     * */
    @Test
    public void findDtoByQueryProjection() throws Exception{
        // given
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * 동적 쿼리는 두가지 방법이 있다.
     * BooleanBuilder 사용
     * Where 다중 파라미터 사용
     * */
    @Test
    public void dynamicQuery_BooleanBuilder() throws Exception{
        // given
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
        // 밑에 처럼 필수로 오는 값이면 초기값으로 넣어줘도 된다.
//        BooleanBuilder builder = new BooleanBuilder(member.username.eq(usernameCond));
        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }
        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }
        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }
    /**
     * 추천!!!
     * 동적쿼리 Where 다중 파라미터 사용 (훨씬 좋아 실무서 많이 쓴다.)
     * 이것에 특징은 where 에 null 이들어가면 그냥 무시 된다. 그래서 추천!!!
     * 이것의 어마어마한 장점은 조립을 따로해서 한방에 한 메서드로 사용가능하다는 것이다.
     * */
    @Test
    public void dynamicQuery_WhereParam() throws Exception{
        // given
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);

    }
    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
//                .where(usernameEq(usernameCond), ageEq(ageCond))
                // 아래처럼 조립을 한 메소드를 사용할 수도 있다.
                .where(allEq(usernameCond,ageCond))
                .fetch();
    }
//    private Predicate usernameEq(String usernameCond) { // 조립하려면 BooleanExpression 을 쓰는 것이 좋아 BooleanExpression 을 쓰자!
    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }
//    private Predicate ageEq(Integer ageCond) {
    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }
    // 조립을 한 메서드
    // 참고 : 광고가 상태가 isValid, 날짜가 IN: isServiceable 이여야하고 등등 의 조건들을 조립할 수 있다.
    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
//    참고 이런식으로  return isValid(usernameCond).and(dateIn(ageCond));
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    /**
     * 수정, 삭제 벌크 연산 - 배치 쿼리 -> 여러 데이터를 한쿼리로 수행   .execute(); 사용
     * 벌크연산은 영속성컨텍스트와 DB 와의 동기화를 주의해야한다.
     * 따라서 벌크연산 후에는 초기화를 해주자
     *
     * JPA에서는 주로 변경감지(dirty checking)을 통해 데이터의 update 가 일어나는데
     * 이 경우에는 개별 엔티티에 대해서 건마다 쿼리가 나가기 때문에 쿼리 한번으로 대량 데이터를 수정할 때는 벌크연산을 사용한다 .
     * */
    @Test
    public void bulkUpdate() throws Exception{
        // given
        // member1 = 10 -> 비회원 , member2 = 20 -> 비회원 // 즉 28살 이하의 회원이면 이름을 비회원트로 바꿔라
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();
        // 벌크연산은 영속성컨텍스트와 디비와의 상태가 맞지 않게된다. 따라서 벌크연산 후에는 아래와 같이 영속성컨텍스트를 한번 초기화 시켜줘야한다.
        em.flush();
        em.clear();
        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();
        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }
    }

    @Test
    public void bulkAdd() throws Exception{
        // given
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
//                .set(member.age, member.age.multiply(2))
//                .set(member.age, member.age.add(-1))
                .execute();
    // 빼기는 -1 로 - 를 붙여서
        //곱하기는 multiply() 로
    }

    @Test
    public void bulkDelete() throws Exception {
        // given
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }

    /**
     * SQL function 호출하기
     * 회원 member 라는 단어를 m 으로 바꿔서 조회
     * 이 function 기능은 Dialect 에 등록되어 있어야한다.
     * 커스텀된 function 을 하고싶다면 이 Dialect 를 상속받아 등록시켜야한다.
     * */
    @Test
    public void sqlFunction() throws Exception{
        // given
        List<String> result = queryFactory
                .select(
                        Expressions.stringTemplate("function('replace',{0},{1},{2})",
                                member.username,"member" ,"m")
                )
                .from(member)
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
    @Test
    public void sqlFunction2() throws Exception{
        // given
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
//                .where(member.username.eq(
//                        Expressions.stringTemplate(
//                                "function('lower',{0})",
//                                member.username
//                        )
//                ))
                //Ansi 표준 함숨들은 QueryDsl 도 이런식으로 내장하고 있다.
                .where(member.username.eq(member.username.lower()))
                .fetch();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }
}
