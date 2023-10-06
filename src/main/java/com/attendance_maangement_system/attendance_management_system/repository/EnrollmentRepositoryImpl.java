package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.attendance_maangement_system.attendance_management_system.domain.Enrollment;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

@Repository
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_BY_ID = "SELECT ENROLLMENTID , COURSEID , STUDENTID , ENROLLMENTDATE FROM ENROLLMENT WHERE ENROLLMENTID=?;";
    private static final String SQL_CREATE = "INSERT INTO ENROLLMENT(COURSEID , STUDENTID , ENROLLMENTDATE) VALUES(? , ? , ?);";
    private static final String SQL_DELETE = "DELETE FROM ENROLLMENT WHERE COURSEID=? AND STUDENTID=?;";

    @Override
    public Enrollment findById(Integer enrollmentId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, enrollmentRowMapper, new Object[] { enrollmentId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("No enrollment found");
        }
    }

    @Override
    public Integer create(Integer courseId, Integer studentId, Date date) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, courseId);
                ps.setInt(2, studentId);
                ps.setDate(3, date);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (DataIntegrityViolationException e) {

            if (e.getMessage().contains("Duplicate"))
                throw new InvalidRequestException("Student already enrolled.");
            else if (e.getMessage().contains("foreign key"))
                throw new InvalidRequestException("Student and/or course may not exist.");
            else
                throw new InvalidRequestException("Invalid Request.");
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request.");
        }
    }

    @Override
    public void delete(Integer courseId, Integer studentId) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_DELETE, new Object[] { courseId, studentId });
        } catch (Exception e) {
            throw new InvalidRequestException("invalid request");
        }
    }

    private RowMapper<Enrollment> enrollmentRowMapper = ((rs, rowNum) -> {
        return new Enrollment(
                rs.getInt("ENROLLMENTID"),
                rs.getInt("COURSEID"),
                rs.getInt("STUDENTID"),
                rs.getDate("ENROLLMENTDATE"));
    });

}
