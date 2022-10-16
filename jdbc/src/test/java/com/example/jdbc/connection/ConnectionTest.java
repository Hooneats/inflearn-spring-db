package com.example.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.jdbc.connection.ConnectionConst.*;

public class ConnectionTest {


    /**
     * DriverManager 사용 -> 설정과 사용이 분리되지 않았다.
     */
    @DisplayName("DriverManager 는 설정과 사용이 분리되지 않았다.")
    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("con1 = " + con1);
        System.out.println("con2 = " + con2);
    }


    /**
     * DataSource 사용 설정과 사용이 분리
     */
    @DisplayName("DataSourceDriverManager 사용.")
    @Test
    void dataSourceDriverManager() throws SQLException {
        // DataSourceDriverManager 는 항상 새로운 커넥션을 설정
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        // DataSourceDriverManager 는 항상 새로운 커넥션을 획득
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        // DataSourceDriverManager 는 항상 새로운 커넥션을 획득 검증
        /**
         * 결과
         * con1 = conn0: url=jdbc:h2:mem:testdb user=SA
         * con2 = conn1: url=jdbc:h2:mem:testdb user=SA
         * ==> 설정과 사용을 분리한 것이다!
         */
        System.out.println("con1 = " + con1);
        System.out.println("con2 = " + con2);
    }

    @DisplayName("HikariDataSource 를 사용해 Connection Pool 사용")
    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setPoolName("MyHikariPool");

        useDataSource(hikariDataSource);
        Thread.sleep(1000);
        /**
         * 결과
         * 22:02:52.944 [MyHikariPool connection adder]
         * DEBUG com.zaxxer.hikari.pool.HikariPool - MyHikariPool - After adding stats (total=10, active=2, idle=8, waiting=0)
         */
    }
}
