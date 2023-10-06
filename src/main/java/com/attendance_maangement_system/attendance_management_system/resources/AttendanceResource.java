package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.domain.Attendance;
import com.attendance_maangement_system.attendance_management_system.services.AttendanceService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceResource {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/markAttendance") // POST /api/attendance/{studentId}
    public ResponseEntity<Map<String, Object>> markAttendance(HttpServletRequest request,
            @RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap<>();
        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObject.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObject.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
        try {
            Date date = new java.sql.Date(System.currentTimeMillis());
            Map<String, Object> studentData = (Map<String, Object>) map.get("studentData");
            int courseId = (Integer) map.get("courseId");
            Attendance newAttendance = attendanceService.addAttendance(studentData, courseId, date);
            returnObject.put("data", newAttendance);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<Map<String, Object>> fetchAttendanceById(HttpServletRequest request,
            @PathVariable("attendanceId") Integer attendanceId) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            Attendance attendance = attendanceService.fetchAttendance(attendanceId);
            returnObject.put("data", attendance);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/getReport")
    public ResponseEntity<Map<String, Object>> fetchAttendanceReport(HttpServletRequest request,
            @RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            Map<String, Object> studentData = (Map<String, Object>) map.get("studentData");
            List<Map<String, Integer>> dateList = attendanceService.fetchAttendanceRecordByStudentId(studentData);
            returnObject.put("data", dateList);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/getReportByCourseId")
    public ResponseEntity<Map<String, Object>> fetchAttendanceReportByCourseId(HttpServletRequest request,
            @RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap<>();

        try {
            Map<String, Object> studentData = (Map<String, Object>) map.get("studentData");
            int courseId = (Integer) map.get("courseId");
            List<Date> dateList = attendanceService.fetchAttendanceRecordByCourseId(studentData, courseId);
            int count = dateList.size();
            returnObject.put("count", count);
            returnObject.put("dateList", dateList);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }
}
