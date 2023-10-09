package com.attendance_maangement_system.attendance_management_system.services;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.attendance_maangement_system.attendance_management_system.domain.Attendance;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface AttendanceService {
        Attendance fetchAttendance(Object attendanceId) throws ResourceNotFoundException;

        Attendance addAttendance(Map<String, Object> studentData, Integer courseId, Date date)
                        throws InvalidRequestException;

        List<Map<String, Object>> fetchAttendanceRecordByStudentId(Map<String, Object> studentData)
                        throws ResourceNotFoundException;

        List<Date> fetchAttendanceRecordByCourseId(Map<String, Object> studentData, Integer courseId)
                        throws ResourceNotFoundException;
}
