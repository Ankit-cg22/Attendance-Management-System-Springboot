package com.attendance_maangement_system.attendance_management_system.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.attendance_maangement_system.attendance_management_system.domain.User;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_MAKE_ADMIN = "UPDATE USER SET ROLE='admin' WHERE USERID=?;";
    private static final String SQL_FETCH_ADMIN_REQUESTS = "SELECT USERID , FIRSTNAME , LASTNAME , EMAIL,ROLE FROM USER WHERE ROLE = \"admin-request\";";

    @Override
    public void makeAdmin(Integer userId) {
        jdbcTemplate.update(SQL_MAKE_ADMIN, new Object[] { userId });
    }

    @Override
    public List<User> fetchAdminRequests() {
        return jdbcTemplate.query(SQL_FETCH_ADMIN_REQUESTS, userRowMapper, new Object[] {});
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(
                rs.getInt("USERID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("ROLE"));
    });
}
