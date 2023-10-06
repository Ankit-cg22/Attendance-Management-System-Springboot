package com.attendance_maangement_system.attendance_management_system.services;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.attendance_maangement_system.attendance_management_system.domain.Attendance;
import com.attendance_maangement_system.attendance_management_system.repository.AttendanceRepository;

// @Service
public class BiometricAttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public Attendance fetchAttendance(Object attendanceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchAttendance'");
    }

    @Override
    public Attendance addAttendance(Map<String, Object> studentData, Integer courseId, Date date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAttendance'");
    }

    @Override
    public List<Map<String, Integer>> fetchAttendanceRecordByStudentId(Map<String, Object> studentData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchAttendanceRecordByStudentId'");
    }

    @Override
    public List<Date> fetchAttendanceRecordByCourseId(Map<String, Object> studentData, Integer courseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchAttendanceRecordByCourseId'");
    }

}
