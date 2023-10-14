package com.attendance_maangement_system.attendance_management_system.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.repository.StatisticsRepository;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    StatisticsRepository statisticsRepository;

    @Override
    public List<Map<String, Object>> fetchEnrollmentStatsOfLast5Months() {
        try {
            return statisticsRepository.fetchAttendanceCountInLast5Months();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> fetchAttendanceStatsForStudentCourseWise(Integer studentId, Integer courseId) {
        try {
            return statisticsRepository.fetchAttendanceCountMonthWiseForStudent(studentId, courseId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> fetchMonthWiseAttendanceCountForCourse(Integer courseId) {
        try {
            return statisticsRepository.fetchMonthWiseAttendanceCountForCourse(courseId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> fetchCourseWiseAttendanceOfStudentInAMonth(Integer studentId,
            Integer monthNumber) {
        try {
            return statisticsRepository.fetchCourseWiseAttendanceOfStudentInAMonth(studentId, monthNumber);
        } catch (Exception e) {
            throw e;
        }
    }

}
