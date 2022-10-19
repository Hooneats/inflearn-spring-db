package com.example.jdbc.exception.basic.translator;

/**
 * DB 에서는 에러 코드를 던진다 이를 통해 어떤 에러인지 알 수 있는데,
 * 키 중복 에러인경우 키를 변환해 에러 없이 저장하는 복구 로직을 넣을 수 있는 로직을 만들어보자.
 * ErrorCode
 *  ㄴ h2 : 23505
 *  ㄴ MySQL : 1062
 *
 *  TODO : 그러나 위의 문제는 에러코드가 셀수 없이 많고, DB 마다 다르다
 *      ㄴ 이를 위해 스프링은 데이터 접근 예외를 추상화했다. 다음과 같다. (Transient 는 일시적이란 뜻으로 재시도시 성공할 가능성이 있다.)
 *                                      <스프링의 데이터 접근 예외 추상화 일부>
 *                                           RunTimeException
 *                                                  |
 *                                          DataAccessException
 *                                                  |
 *              NonTransientDataAccessException ------------------------------- TransientDataAccessException
 *                              |                                                               |
 *      BadSqlGrammarException ----------- DataIntegrityViolationException                      |
 *                                                  |                                           |
 *                                        DuplicateKeyException                                 |
 *                                                                                              |
 *                                                                                              |
 *                                       QueryTimeoutException -------------- OptimisticLockingFailureException ----------- PessimisticLockingFailureException
 */
public class ExTranslatorV1Test {

}
