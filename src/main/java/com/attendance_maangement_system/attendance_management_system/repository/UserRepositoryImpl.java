package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.Statement;

import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;

import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.exceptions.AuthException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_CREATE = "INSERT INTO USER(FIRSTNAME,LASTNAME , EMAIL , PASSWORD , ROLE) VALUES( ? , ? , ? , ?  , ?);";

    private static final String SQL_FIND_BY_EMAIL = "SELECT USERID , FIRSTNAME , LASTNAME , EMAIL , PASSWORD , ROLE " +
            "FROM USER " +
            "WHERE EMAIL =?";

    private static final String SQL_FIND_BY_ID = "SELECT USERID , FIRSTNAME , LASTNAME , EMAIL , PASSWORD , ROLE " +
            "FROM USER " +
            "WHERE USERID =?";

    private static final String SQL_UPDATE = "UPDATE USER SET FIRSTNAME=? , LASTNAME=? , EMAIL=? , PASSWORD =? WHERE USERID=?;";

    @Override
    public Integer create(String firstName, String lastName, String email, String password, String role)
            throws AuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                ps.setString(5, role);
                return ps;
            }, keyHolder);

            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            throw new AuthException("Invalid details , Failed to create account.");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws AuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, new Object[] { email });

            // we got the user , now compare the passwords
            if (!BCrypt.checkpw(password, user.getPassword())) {
                // we defined getter for password ,which we are using here
                throw new AuthException("Invalid email/password");
            }

            // password matched so return user
            return user;

        } catch (EmptyResultDataAccessException e) {
            throw new AuthException("Invalid email/password.");
        }
    }

    @Override
    public void updateUser(Integer userId, User user) {
        try {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
            jdbcTemplate.update(SQL_UPDATE, new Object[] { user.getFirstName(), user.getLastName(), user.getEmail(),
                    hashedPassword, userId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("User does not exist.");
        }
    }

    @Override
    public User findByUserId(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, new Object[] { userId });
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(
                rs.getInt("USERID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("ROLE"));
    });

}
