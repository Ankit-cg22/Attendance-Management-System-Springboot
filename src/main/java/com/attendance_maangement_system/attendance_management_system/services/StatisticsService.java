package com.attendance_maangement_system.attendance_management_system.services;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    List<Map<String, Object>> fetchEnrollmentStatsOfLast5Months();

    List<Map<String, Object>> fetchAttendanceStatsForStudentCourseWise(Integer studentId, Integer courseId);

    List<Map<String, Object>> fetchMonthWiseAttendanceCountForCourse(Integer courseId);

    List<Map<String, Object>> fetchCourseWiseAttendanceOfStudentInAMonth(Integer studentId, Integer monthNumber);

}
