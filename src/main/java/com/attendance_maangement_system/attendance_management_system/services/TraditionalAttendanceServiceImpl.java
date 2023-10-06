package com.attendance_maangement_system.attendance_management_system.services;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.domain.Attendance;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;
import com.attendance_maangement_system.attendance_management_system.repository.AttendanceRepository;

@Service
public class TraditionalAttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public Attendance fetchAttendance(Object attendanceId) throws ResourceNotFoundException {
        try {
            return attendanceRepository.find((Integer) attendanceId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Attendance addAttendance(Map<String, Object> studentData, Integer courseId, Date date) {
        int studentId = (Integer) studentData.get("studentId");
        int attendanceId = attendanceRepository.create((Integer) studentId, courseId, date);
        return attendanceRepository.find(attendanceId);
    }

    @Override
    public List<Map<String, Integer>> fetchAttendanceRecordByStudentId(Map<String, Object> studentData) {
        int studentId = (Integer) studentData.get("studentId");
        return attendanceRepository.fetchReportForStudentId((Integer) studentId);
    }

    @Override
    public List<Date> fetchAttendanceRecordByCourseId(Map<String, Object> studentData, Integer courseId) {
        int studentId = (Integer) studentData.get("studentId");
        return attendanceRepository.fetchDatesForStudentIdCourseId(studentId, courseId);
    }

}
