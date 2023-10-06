package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.attendance_maangement_system.attendance_management_system.domain.Attendance;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface AttendanceRepository {
    Attendance find(Integer attendanceID) throws ResourceNotFoundException;

    Integer create(Integer studentId, Integer courseId, Date date) throws InvalidRequestException;

    List<Map<String, Integer>> fetchReportForStudentId(Integer studentId) throws ResourceNotFoundException;

    List<Date> fetchDatesForStudentIdCourseId(Integer studentId, Integer courseId) throws ResourceNotFoundException;
}
