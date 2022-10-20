package com.example.jdbc.repository;

import com.example.jdbc.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.example.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.properties")
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager - 항상 새로운 커넥션을 획득 -> conn 이 증가하는 것을 보임
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 기본 HikariDataSource - HikariPool 사용 -> conn 을 pool 에서 꺼내서 재사용하기에 conn 이 같음
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyHikariPool");

        repository = new MemberRepositoryV1(dataSource);
    }

    @DisplayName("JDBC 를 이용한 CRUD")
    @Test
    void jdbcCRUD() throws SQLException {
        Member member = new Member("memberV0", 10000);
        repository.save(member);

        Member findMember = repository.findById(member.getMemberId());
        assertThat(member).isEqualTo(findMember);

        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
        assertThatCode(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }


}
