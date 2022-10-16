package com.example.jdbc.repository;

import com.example.jdbc.connection.DBConnectionUtil;
import com.example.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepository {

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
            log.error("db error",e);
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
            log.error("db error",e);
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
        ResultSet rs = null;

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

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("db error",e);
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("db error", e);
                e.printStackTrace();
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("db error", e);
                e.printStackTrace();
            }
        }
    }
}
