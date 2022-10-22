package org.example.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringApplicationTest {

    /**
     * TODO :
     *  트랜젝션 전파와 옵션
     *   'isolation' , 'timeout' , 'readOnly' 는 트랜젝션이 처음 시작될때만 적용되기에, 트랜젝션에 참여하는 경우에는 적용되지 않는다.
     *      ㄴ 예를 들어 propagation 이 'REQUIRED' 를 통한 생성 시점, 또는 'REQUIRES_NEW' 를 통해 새로 생성한 시점에만 적용된다.
     */

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

    /**
     * TODO :
     *  propagation 옵션
     *      - REQUIRED : 기존 트랜젝션이 있으면 참여 없으면 생성
     *      - REQUIRES_NEW : 항상 새로운 트랜젝션 생성
     *      - SUPPORT : 기존 트랜젝션이 없으면 없는대로 진행, 있으면 참여
     *      - NOT_SUPPORT : 항상 트랜젝션 없이 진행. 기존 트랜젝션이 있을경우 이를 보류
     *      - MANDATORY : 의무사항, 트랜젝션이 반드시 있어야한다. 기존 트랜젝션이 없으면 에외가 발생
     *      - NEVER : 트랜젝션을 사용하지 않는다. 이는 기존 트랜젝션이 있으면 예외 발생
     *      - NESTED : 기존 트랜젝션이 없으면 생성, 있으면 중첩 트랜젝션을 만든다.
     *             ㄴ 중첩 트랜젝션은 외부 트랜젝션의 영향을 받지만, 외부에 영향을 주지 않는다.
     *              ㄴ 중첩 트랜젝션이 롤백 되어도 외부 트랜젝션은 커밋 가능
     *               ㄴ 외부 트랜젝션이 롤백 되면 중첩 트랜젝션도 함께 롤백
     *               ㄴ 이는 JPA 사용할 떄 사용불가, JDBC savepoint 기능을 사용한다. 필요시 DB 드라이버가 해당 기능 지원하는지 확인필요
     */

}
