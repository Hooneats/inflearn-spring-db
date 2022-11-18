package org.example.data.jpa.repository;

import org.example.data.jpa.dto.MemberDto;
import org.example.data.jpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA
 * */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername")
        // NamedQuery 와 메소드 이름을 맞췄기에 지금 이 메소드는 @Query 없어도 되긴된다.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new org.example.data.jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberByUsername(String username); // 단건

    Optional<Member> findOptionalByUsername(String username); // 단건

    /**
     * Spring Data Jpa 페이지 처리
     * 사실 TotalCount 는 성능상 좀 안좋을 수 있다. 그래서 totalCount 를 분리하는 방법도 제공한다.
     * @Query(value = "select m from Member m left join m.team t"
     *          , countQuery = "select count(m.username) from Member m")
     *          이렇게 countQuery 를 분리하면 countQuery 에 join 이 없기에 성능상 좋다.
     */
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);

    // Select 인 result 를 쓰는게아닌 아닌 executeUpdate 를 쓰려면 @Modifying 있어야한다.
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // Fetch join 사용 N + 1 문제 해결
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // @EntityGraph로 Fetch join 사용 N + 1 문제 해결
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"}) // 이렇게 JPQL 에다가 @EntityGraph를 추가할수 있다.
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"}) // 메소드 생성에서도 @EntityGraph 가능
//    @EntityGraph("Member.all") // NamedEntityGraph 로 사용(잘 안씀)
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // JPA Hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // JPA Lock - > select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

//    // 인터페이스 projections
//    List<UsernameOnly> findProjectionByUsername(@Param("username") String username);
    // DTO projections
//    List<UsernameOnlyDto> findProjectionByUsername(@Param("username") String username);
    <T> List<T> findProjectionByUsername(@Param("username") String username, Class<T> type);

    // JPA 네이티브 쿼리
    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    // Jpa 네이티브쿼기 -> projections
    @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
            "from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);

}

