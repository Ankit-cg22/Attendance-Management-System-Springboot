package com.attendance_maangement_system.attendance_management_system.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String FETCH_ATTENDANCE_COUNT_IN_LAST_5_MONTHS = "SELECT C.COURSEID, C.COURSETITLE, COUNT(E.ENROLLMENTID) AS 'ENROLLMENTCOUNT' FROM COURSE C LEFT JOIN ENROLLMENT E ON C.COURSEID = E.COURSEID AND E.ENROLLMENTDATE >= DATE_SUB(CURDATE(), INTERVAL 5 MONTH)  GROUP BY C.COURSEID, C.COURSETITLE;";

    private static final String FETCH_MONTHWISE_ATTENDANCE_FOR_STUDENT_IN_COURSE = "WITH Months AS ( " +
            "  SELECT 1 AS Month " +
            "  UNION SELECT 2 " +
            "  UNION SELECT 3 " +
            "  UNION SELECT 4 " +
            "  UNION SELECT 5 " +
            "  UNION SELECT 6 " +
            "  UNION SELECT 7 " +
            "  UNION SELECT 8 " +
            "  UNION SELECT 9 " +
            "  UNION SELECT 10 " +
            "  UNION SELECT 11 " +
            "  UNION SELECT 12 " +
            ") " +
            "SELECT " +
            "  M.Month AS 'MONTH', " +
            "  IFNULL(COUNT(A.date), 0) AS 'ATTENDANCECOUNT' " +
            "FROM Months M " +
            "LEFT JOIN Attendance A ON MONTH(A.date) = M.Month " +
            "                      AND YEAR(A.date) = YEAR(CURDATE()) " +
            "                      AND A.courseid = ? " +
            "                      AND A.studentid = ? " +
            "GROUP BY M.Month";

    private static final String FETCH_MONTH_WISE_ATTENDANCE_FOR_COURSE = """
            WITH Months AS (
                SELECT 1 AS Month
                UNION SELECT 2
                UNION SELECT 3
                UNION SELECT 4
                UNION SELECT 5
                UNION SELECT 6
                UNION SELECT 7
                UNION SELECT 8
                UNION SELECT 9
                UNION SELECT 10
                UNION SELECT 11
                UNION SELECT 12
              )

              SELECT
                M.Month AS 'MONTH',
                IFNULL(COUNT(A.attendanceId), 0) AS 'ATTENDANCECOUNT'
              FROM Months M
              LEFT JOIN attendance A ON MONTH(A.date) = M.Month
                                    AND YEAR(A.date) = YEAR(CURDATE())
                                    AND A.courseid = ?
              GROUP BY M.Month;
              """;

    private static final String FETCH_COURSE_WISE_ATTENDANCE_IN_A_MONTH = """
            WITH ENROLLEDCOURSES
            AS (
                SELECT E.COURSEID , C.COURSETITLE
                FROM ENROLLMENT E
                JOIN COURSE C
                USING (COURSEID)
                WHERE E.STUDENTID = ?
            )
            SELECT EC.COURSEID , EC.COURSETITLE , COUNT(A.DATE) AS "ATTENDANCECOUNT"
            FROM ENROLLEDCOURSES EC
            LEFT JOIN ATTENDANCE A
            ON EC.COURSEID = A.COURSEID AND STUDENTID = ? AND MONTH(A.DATE) = ? AND YEAR(A.DATE) = YEAR(CURDATE())
            GROUP BY EC.COURSEID ;
                        """;

    @Override
    public List<Map<String, Object>> fetchAttendanceCountInLast5Months() {
        try {
            return jdbcTemplate.query(FETCH_ATTENDANCE_COUNT_IN_LAST_5_MONTHS, enrollmentStatsMapper, new Object[] {});
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> fetchAttendanceCountMonthWiseForStudent(Integer studentId, Integer courseId) {
        try {
            return jdbcTemplate.query(FETCH_MONTHWISE_ATTENDANCE_FOR_STUDENT_IN_COURSE, studentCourseStatsMapper,
                    new Object[] { courseId, studentId });
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> fetchMonthWiseAttendanceCountForCourse(Integer courseId) {
        try {
            return jdbcTemplate.query(FETCH_MONTH_WISE_ATTENDANCE_FOR_COURSE, courseStatsRowMapper,
                    new Object[] { courseId });
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> fetchCourseWiseAttendanceOfStudentInAMonth(Integer studentId,
            Integer monthNumber) {
        try {
            return jdbcTemplate.query(FETCH_COURSE_WISE_ATTENDANCE_IN_A_MONTH, courseWiseStudentStatsMapper,
                    new Object[] { studentId, studentId, monthNumber });
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw e;
        }
    }

    private RowMapper<Map<String, Object>> enrollmentStatsMapper = ((rs, rowNum) -> {
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", rs.getInt("COURSEID"));
        map.put("courseTitle", rs.getString("COURSETITLE"));
        map.put("enrollmentCount", rs.getInt("ENROLLMENTCOUNT"));
        return map;
    });

    private RowMapper<Map<String, Object>> studentCourseStatsMapper = ((rs, rowNum) -> {
        Map<String, Object> map = new HashMap<>();
        map.put("month", rs.getInt("MONTH"));
        map.put("attendanceCount", rs.getInt("ATTENDANCECOUNT"));
        return map;
    });

    private RowMapper<Map<String, Object>> courseStatsRowMapper = ((rs, rowNum) -> {
        Map<String, Object> map = new HashMap();
        map.put("month", rs.getInt("MONTH"));
        map.put("attendanceCount", rs.getInt("ATTENDANCECOUNT"));
        return map;
    });

    private RowMapper<Map<String, Object>> courseWiseStudentStatsMapper = ((rs, rowNum) -> {
        Map<String, Object> map = new HashMap();
        map.put("courseTitle", rs.getString("COURSETITLE"));
        map.put("attendanceCount", rs.getInt("ATTENDANCECOUNT"));
        return map;
    });

}
