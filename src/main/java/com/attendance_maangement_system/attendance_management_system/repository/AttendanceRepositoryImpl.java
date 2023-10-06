package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.attendance_maangement_system.attendance_management_system.domain.Attendance;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

@Repository
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final static String SQL_FIND_BY_ID = "SELECT ATTENDANCEID , STUDENTID , COURSEID , DATE FROM ATTENDANCE WHERE ATTENDANCEID = ?;";
    private final static String SQL_CREATE = "INSERT INTO ATTENDANCE(STUDENTID , COURSEID ,  DATE) VALUES(? , ? , ?);";
    private final static String SQL_REPORT_BY_STUDENTID = "SELECT E.COURSEID , COUNT(DATE) AS 'PRESENTCOUNT' " +
            "FROM ENROLLMENT E LEFT JOIN ATTENDANCE A " +
            "ON E.STUDENTID = A.STUDENTID AND E.COURSEID = A.COURSEID " +
            "WHERE E.STUDENTID = ? " +
            "GROUP BY E.COURSEID ;";
    private final static String SQL_FETCH_DATES_BY_STUDENTID_COURSEID = "SELECT DATE FROM ATTENDANCE WHERE STUDENTID = ? AND COURSEID = ? ;";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Attendance find(Integer attendanceID) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, attendanceRowMapper, new Object[] { attendanceID });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Attendance not found");
        }
    }

    @Override
    public Integer create(Integer studentId, Integer courseId, Date date) throws InvalidRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setDate(3, date);
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
                throw new InvalidRequestException("Attendance already marked for today.");
            else if (e.getMessage().contains("foreign key"))
                throw new InvalidRequestException(
                        "Either student and/or course does not exist . Or student is not enrolled in the course.");
            else
                throw new InvalidRequestException("Invalid request");
        }
    }

    @Override
    public List<Map<String, Integer>> fetchReportForStudentId(Integer studentId) throws ResourceNotFoundException {
        try {
            return jdbcTemplate.query(SQL_REPORT_BY_STUDENTID, attendanceByCourseIdRowMapper,
                    new Object[] { studentId });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResourceNotFoundException("Student not found");
        }
    }

    @Override
    public List<Date> fetchDatesForStudentIdCourseId(Integer studentId, Integer courseId)
            throws ResourceNotFoundException {
        try {
            return jdbcTemplate.query(SQL_FETCH_DATES_BY_STUDENTID_COURSEID, dateRowMapper,
                    new Object[] { studentId, courseId });
        } catch (Exception e) {
            throw new ResourceNotFoundException("Student not found");
        }
    }

    private RowMapper<Attendance> attendanceRowMapper = ((rs, rowNum) -> {
        return new Attendance(rs.getInt("ATTENDANCEID"),
                rs.getInt("STUDENTID"),
                rs.getInt("COURSEID"),
                rs.getDate("DATE"));
    });

    private RowMapper<Map<String, Integer>> attendanceByCourseIdRowMapper = ((rs, rowNum) -> {
        Map<String, Integer> map = new HashMap<>();
        map.put("couseId", rs.getInt("COURSEID"));
        map.put("presentCount", rs.getInt("PRESENTCOUNT"));
        return map;
    });

    private RowMapper<Date> dateRowMapper = ((rs, rowNum) -> {
        Date date = rs.getDate("DATE");
        return date;
    });
}
