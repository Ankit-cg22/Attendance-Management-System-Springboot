package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    // Fetch all students
    private static final String SQL_FIND_ALL = "SELECT STUDENTID ,FIRSTNAME ,LASTNAME ,EMAIL FROM STUDENT; ";

    // Add a student
    private static final String SQL_CREATE = "INSERT INTO STUDENT(USERID ,FIRSTNAME, LASTNAME , EMAIL) VALUES(? ,? , ? , ?);";

    // Find by studentId
    private static final String SQL_FIND_BY_ID = "SELECT STUDENTID , FIRSTNAME , LASTNAME , EMAIL FROM STUDENT WHERE STUDENTID=?;";

    // Delete by studentId
    private static final String SQL_DELETE = "DELETE FROM STUDENT WHERE STUDENTID = ?";

    // Update by studentId
    private static final String SQL_UPDATE = "UPDATE STUDENT SET FIRSTNAME=?, LASTNAME = ? , EMAIL = ? WHERE STUDENTID = ?";

    // fetch enrolled courses
    private static final String SQL_FETCH_ENROLLED_COURSES = "SELECT DISTINCT COURSEID FROM ENROLLMENT WHERE STUDENTID = ?;";

    // fetch studentid for particular userid
    private static final String SQL_FETCH_STUDENTID_FOR_USERID = "SELECT STUDENTID FROM STUDENT WHERE USERID = ?;";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Student> findAll() throws RuntimeException {
        try {
            return jdbcTemplate.query(SQL_FIND_ALL, studentRowMapper, new Object[] {});
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Student findById(Integer studentId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, studentRowMapper, new Object[] { studentId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Student not found");
        }
    }

    @Override
    public Integer create(Integer userId, String firstName, String lastName, String email)
            throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, email);
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
    public void update(Integer studentId, Student student) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE,
                    new Object[] { student.getFirstName(), student.getLastName(), student.getEmail(), studentId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid request.");
        }
    }

    @Override
    public void removeById(Integer studentId) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_DELETE, new Object[] { studentId });
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Integer> findEnrolledCourses(Integer studentId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.query(SQL_FETCH_ENROLLED_COURSES, integerRowMapper, new Object[] { studentId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Data unavailable");
        }
    }

    @Override
    public Integer getStudentIdFromUserId(Integer userId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FETCH_STUDENTID_FOR_USERID, studentIdRowMapper,
                    new Object[] { userId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("No such student.");
        }
    }

    private RowMapper<Student> studentRowMapper = ((rs, rowNum) -> {
        return new Student(rs.getInt("STUDENTID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"));
    });

    private RowMapper<Integer> integerRowMapper = ((rs, rowNum) -> {
        return rs.getInt("COURSEID");
    });

    private RowMapper<Integer> studentIdRowMapper = ((rs, rowNum) -> {
        return rs.getInt("STUDENTID");
    });

}