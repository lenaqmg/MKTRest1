package com.netcracker.solutions.mkt.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    // Spring сам подставит jdbcTemplate из XML
    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigInteger findUserIdByLogin(String login) {
        String sql = "select object_id from nc_objects where object_type_id = 1 and name = ?";

        BigDecimal bd = jdbcTemplate.queryForObject(sql, new Object[]{login}, BigDecimal.class);
        return bd != null ? bd.toBigInteger() : null;
    }

    public Employee getEmployee(BigInteger userId) {
        String sql = "select o.object_id, o.name from nc_references r, nc_objects o\n" +
                "where r.attr_id = 9103061512013847760 and r.reference = ?\n" +
                "and o.object_id = r.object_id";
        List<Employee> employees = jdbcTemplate.query(sql, new Object[]{new BigDecimal(userId)}, new RowMapper<Employee>() {
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Employee(
                        rs.getBigDecimal("object_id").toBigInteger(),
                        rs.getString("name")
                );
            }
        });
        return employees == null ? null : employees.isEmpty() ? null : employees.get(0);
    }

    public BigInteger putOperation(Integer key, String date, BigInteger user_id) {
        String sql = "insert into operations (operaion_key, operation_date, user_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{key, date, new BigDecimal(user_id)});
        return BigInteger.valueOf(3443L);
    }
}

