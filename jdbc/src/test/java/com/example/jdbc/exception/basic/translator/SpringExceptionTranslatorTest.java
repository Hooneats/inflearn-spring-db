package com.example.jdbc.exception.basic.translator;

import static com.example.jdbc.connection.ConnectionConst.*;

import com.example.jdbc.connection.ConnectionConst;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.test.context.TestPropertySource;

/**
 * TODO : sql-error-codes.xml 파일에서 이러한 에러들을 거른다.
 *                                      <스프링의 데이터 접근 예외 추상화 일부>
 *                                             RunTimeException
 *                                                    |
 *                                            DataAccessException
 *                                                   |
 *               NonTransientDataAccessException ------------------------------- TransientDataAccessException
 *                               |                                                               |
 *       BadSqlGrammarException ----------- DataIntegrityViolationException                      |
 *                                                   |                                           |
 *                                         DuplicateKeyException                                 |
 *                                                                                               |
 *                                                                                               |
 *                                        QueryTimeoutException -------------- OptimisticLockingFailureException ----------- PessimisticLockingFailureException
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
public class SpringExceptionTranslatorTest {

    DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    /**
     * translator.translate("select", sql, e);
     * 첫번쨰는 읽을 수 있는 설명, 두번쨰는 실행한 sql , 마지막은 SQLException 을 전달하면 된다.
     * sql-error-codes.xml 파일에서 이러한 에러들을 거른다.
     */
    @DisplayName("스프링의 BadSqlGrammarException.class")
    @Test
    void exceptionTranslator() {
        String sql = "select bad grammer";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(
                dataSource);
            DataAccessException resultEx = translator.translate("select", sql, e);
            System.out.println("resultEx = " + resultEx);
            Assertions.assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }
}
