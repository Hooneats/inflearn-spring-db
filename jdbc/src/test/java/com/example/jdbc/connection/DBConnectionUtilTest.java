package com.example.jdbc.connection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

class DBConnectionUtilTest {

    @DisplayName("커넥션 객체를 생성한다.")
    @Test
    void connection() {
        /**
         * DriverManager.getConnection() 에 넘긴 정보를 가지고
         * 라이브러리에 등록된(h2,mysql) 목록에서 접속을 찾아 맞는것을 반환한다.
         */
        Connection connection =
                DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
    }
}