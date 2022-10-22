package org.example.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringApplicationTest {

    /**
     * TODO :
     *  @Transactional 의 옵션
     *      - value , transactionManager
     *      - rollbackFor : 기본은 RuntimeException 과 Error  그 하위 를 롤백 (Exception 과 그 하위는 커밋한다.)
     *           ㄴ rollbackFor = Exception.class 이렇게 지정하면 Exception 및 그 하위 발생또한 롤백한다.
     *      - noRollbackFor ; rollbackFor 와 반대
     *      - propagation : 트랜젝션 전파에 대한 옵션 *중요하다*
     *      - isolation : 격리 수준을 지정할 수 있다. 기본값은 DB 에서 설정한 값을 따르는 DEFAULT
     *              ㄴ DEFAULT
     *              ㄴ READ_UNCOMMITTED
     *              ㄴ READ_COMMITTED
     *              ㄴ REPEATABLE_READ
     *              ㄴ SERIALIZABLE
     *      - timeout : 트랜젝션 수행시간에 대한 타임아웃을 초 단위로 가능, 기본값은 트랜젝션 시스템의 타임아웃을 따른다.
     *                      운영환경에따라 제대로 동작안할수도 있다. - 확인필요
     *      - label : 트랜젝션 어노테이션을 직접 읽어서 특정 값을 쓰고 싶을때 (추가적인 저장소같은필드)
     *      - readOnly : 기본값은 false , readOnly 의 옵션은 크게 3곳에서 적용된다.
     *              ㄴ 프레임워크 - readOnly = true 인 경우, JPA 는 플러시를 하지 않는다.
     *              ㄴ JDBC 드라이버 - readOnly = true 인 경우, 변경 쿼리가 발생하면 예외를 던지거나 또는 읽기 커넥션을 맺는다.(이는 DB와 드라이버에 따라 다르다.)
     *              ㄴ 데이터베이스 - readOnly = true 인 경우, 내부에서 성능 최적화가 발생한다.(이는 DB와 드라이버에 따라 다르다.)
     */

}
