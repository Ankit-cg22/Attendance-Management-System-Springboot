package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.attendance_maangement_system.attendance_management_system.domain.RefreshToken;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_CREATE = "INSERT INTO REFRESH_TOKEN(REFRESHTOKEN , EXPIRY , USERID) VALUES(? , ? , ?);";

    private static final String SQL_FIND = "SELECT REFRESHTOKEN , EXPIRY , USERID FROM REFRESH_TOKEN WHERE REFRESHTOKEN =  ? ;";

    private static final String SQL_DELETE = "DELETE FROM REFRESH_TOKEN WHERE REFRESHTOKEN =  ? ;";

    private static final String SQL_DELETE_BY_USER_ID = "DELETE FROM REFRESH_TOKEN WHERE USERID =  ? ;";

    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshTokenObj) throws RuntimeException {

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, refreshTokenObj.getRefreshToken());
                ps.setTimestamp(2, refreshTokenObj.getExpiry());
                ps.setInt(3, refreshTokenObj.getUserId());
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return findById(generatedKey.toString());
            else
                return null;
        } catch (Exception e) {
            throw new RuntimeException("Internal Error");
        }
    }

    @Override
    public RefreshToken findById(String refreshToken) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND, refreshTokenRowMapper, new Object[] { refreshToken });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Token does not exist.");
        }
    }

    @Override
    public void delete(String refreshToken) {
        try {
            jdbcTemplate.update(SQL_DELETE, new Object[] { refreshToken });
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }

    @Override
    public void deleteByUserId(Integer userId) {
        try {
            jdbcTemplate.update(SQL_DELETE_BY_USER_ID, new Object[] { userId });
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }

    private RowMapper<RefreshToken> refreshTokenRowMapper = ((rs, rowNum) -> {
        return new RefreshToken(
                rs.getString("REFRESHTOKEN"),
                rs.getTimestamp("EXPIRY"),
                rs.getInt("USERID"));
    });

}
