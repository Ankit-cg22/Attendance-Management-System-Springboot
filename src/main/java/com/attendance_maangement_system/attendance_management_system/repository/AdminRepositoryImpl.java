package com.attendance_maangement_system.attendance_management_system.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_MAKE_ADMIN = "UPDATE USER SET ROLE='admin' WHERE USERID=?;";

    @Override
    public void makeAdmin(Integer userId) {
        jdbcTemplate.update(SQL_MAKE_ADMIN, new Object[] { userId });
    }
}
