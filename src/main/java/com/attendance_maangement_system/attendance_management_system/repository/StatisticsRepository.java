package com.attendance_maangement_system.attendance_management_system.repository;

import java.util.List;
import java.util.Map;

public interface StatisticsRepository {
    List<Map<String, Object>> fetchAttendanceCountInLast5Months();

    List<Map<String, Object>> fetchAttendanceCountMonthWiseForStudent(Integer studentId, Integer courseId);

    List<Map<String, Object>> fetchMonthWiseAttendanceCountForCourse(Integer courseId);

    List<Map<String, Object>> fetchCourseWiseAttendanceOfStudentInAMonth(Integer studentId, Integer monthNumber);
}
