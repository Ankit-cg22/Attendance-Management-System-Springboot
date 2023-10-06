package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.attendance_maangement_system.attendance_management_system.domain.Parent;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

@Repository
public class ParentRepositoryImpl implements ParentRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_CREATE = "INSERT INTO PARENT(USERID ,CHILDID , FIRSTNAME, LASTNAME , EMAIL) VALUES(? ,? ,? , ? , ?);";

    private static final String SQL_FIND_BY_ID = "SELECT CHILDID , PARENTID , FIRSTNAME , LASTNAME , EMAIL FROM PARENT WHERE PARENTID=?;";

    private static final String SQL_FETCH_PARENTID_FOR_USERID = "SELECT PARENTID FROM PARENT WHERE USERID = ?;";

    @Override
    public Integer create(Integer userId, Integer childId, String firstName, String lastName, String email)
            throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setInt(2, childId);
                ps.setString(3, firstName);
                ps.setString(4, lastName);
                ps.setString(5, email);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("Duplicate"))
                throw new InvalidRequestException("Email already registered.");
            else
                throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public Integer getParentIdFromUserId(Integer userId) throws ResourceNotFoundException {
        try {
            System.out.println(userId);
            return jdbcTemplate.queryForObject(SQL_FETCH_PARENTID_FOR_USERID, parentIdRowMapper,
                    new Object[] { userId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("No such student.");
        }
    }

    @Override
    public Parent findById(Integer parentId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, parentRowMapper, new Object[] { parentId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Parent not found");
        }
    }

    private RowMapper<Parent> parentRowMapper = ((rs, rowNum) -> {
        return new Parent(rs.getInt("PARENTID"),
                rs.getInt("CHILDID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"));
    });
    private RowMapper<Integer> parentIdRowMapper = ((rs, rowNum) -> {
        return rs.getInt("PARENTID");
    });

}
