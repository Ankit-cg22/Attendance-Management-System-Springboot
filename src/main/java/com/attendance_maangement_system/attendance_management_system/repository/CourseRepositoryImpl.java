package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.attendance_maangement_system.attendance_management_system.domain.Course;
import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Read all
    private static final String SQL_FETCH_ALL = "SELECT COURSEID ,COURSETITLE FROM COURSE;";

    // Read
    private static final String SQL_FIND_BY_ID = "SELECT COURSEID , COURSETITLE FROM COURSE WHERE COURSEID = ? ;";

    // Create
    private static final String SQL_CREATE = "INSERT INTO COURSE(COURSETITLE) VALUES(?);";

    // Delete
    private static final String SQL_DELETE = "DELETE FROM COURSE WHERE COURSEID =  ? ;";

    // Update
    private static final String SQL_UPDATE = "UPDATE COURSE SET COURSETITLE = ? WHERE COURSEID = ?;";

    // Fetch enrolled students
    private static final String SQL_FIND_ENROLLED_STUDENTS = "SELECT S.STUDENTID , U.* , E.COURSEID FROM STUDENT S JOIN ENROLLMENT E ON S.STUDENTID = E.STUDENTID AND E.COURSEID=? JOIN USER U ON S.USERID = U.USERID;";

    // Fetch not enrolled students
    private static final String SQL_FIND_NOT_ENROLLED_STUDENTS = "SELECT S.STUDENTID , U.* , E.COURSEID FROM STUDENT S LEFT JOIN ENROLLMENT E ON S.STUDENTID = E.STUDENTID AND E.COURSEID=? JOIN USER U ON S.USERID = U.USERID  WHERE COURSEID IS NULL;";

    @Override
    public List<Course> fetchAll() throws RuntimeException {
        try {
            return jdbcTemplate.query(SQL_FETCH_ALL, courseRowMapper, new Object[] {});
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Course findById(Integer courseId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, courseRowMapper, new Object[] { courseId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Course not found");
        }
    }

    @Override
    public Integer create(String courseTitle) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, courseTitle);
                return ps;
            }, keyHolder);
            Number generatedKey = keyHolder.getKey();
            if (generatedKey != null)
                return generatedKey.intValue();
            else
                return 0;
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer courseId) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_DELETE, new Object[] { courseId });
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request.");
        }
    }

    @Override
    public void update(Integer courseId, Course course) throws InvalidRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[] { course.getCourseTitle(), courseId });
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request.");
        }
    }

    @Override
    public List<Student> findEnrolledStudents(Integer courseId) {
        try {
            return jdbcTemplate.query(SQL_FIND_ENROLLED_STUDENTS, studentRowMapper, new Object[] { courseId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Data unavailable.");
        }
    }

    @Override
    public List<Student> findNotEnrolledStudents(Integer courseId) {
        try {
            return jdbcTemplate.query(SQL_FIND_NOT_ENROLLED_STUDENTS, studentRowMapper, new Object[] { courseId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Data unavailable.");
        }
    }

    private RowMapper<Course> courseRowMapper = ((rs, rowNum) -> {
        return new Course(
                rs.getInt("courseId"),
                rs.getString("courseTitle"));
    });

    private RowMapper<Student> studentRowMapper = ((rs, rowNum) -> {
        return new Student(
                rs.getInt("USERID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("ROLE"),
                rs.getInt("STUDENTID"));
    });
}
