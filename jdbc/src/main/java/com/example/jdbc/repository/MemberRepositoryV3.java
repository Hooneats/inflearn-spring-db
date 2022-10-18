package com.example.jdbc.repository;

import com.example.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 트랜젝션 - 트랜젝션 매니저
 * DataSourceUtils.getConnection()
 * DataSourceUtils.releaseConnection()
 *  ㄴ releaseConnection 을 사용하면 서비스에서 트랜젝션을 시작했기에 서비스에서 트랜젝션을 닫기전까지 완전히 닫지않는다.
 */
@Slf4j
public class MemberRepositoryV3 {

    private final DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        /**
         * PreparedStatement 를 사용해 values 를 넣어주면 데이터로 인식하기에
         * SQL Injection 공격을 예방할 수 있다!
         */
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member(
                        rs.getString("member_id"),
                        rs.getInt("money")
                );
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int count = pstmt.executeUpdate();
            log.info("updated rows count {}", count);
        } catch (SQLException e) {
            log.error("db error",e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error",e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private Connection getConnection() throws SQLException {
        // 주의! 트랜젝션 동기화를 사용하려면 DataSourceUtils 를 사용해야 한다.
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("### Get connection = {} , class = {}", connection, connection.getClass());
        return connection;
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // 주의! 트랜젝션 동기화를 사용하려면 DataSourceUtils 를 사용해야 한다.
        DataSourceUtils.releaseConnection(con, dataSource);

    }
}
