package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.services.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsResourse {
    @Autowired
    StatisticsService statisticsService;

    @PostMapping("/enrollmentStats")
    public ResponseEntity<Map<String, Object>> fetchEnrollmentStatsOfLast5Months(@RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap();
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
            returnObject.put("data", statisticsService.fetchEnrollmentStatsOfLast5Months());
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", "Internal Error");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/studentAttendancePerCourse")
    public ResponseEntity<Map<String, Object>> fetchAttendanceStatsOfStudentCourseWise(
            @RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap();
        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);
        if (tokenMap.get("valid") == (Boolean) false) {
            returnObject.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObject.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
        Integer studentId = (Integer) map.get("studentId");
        Integer courseId = (Integer) map.get("courseId");

        try {
            returnObject.put("data", statisticsService.fetchAttendanceStatsForStudentCourseWise(studentId, courseId));
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", "Internal Error");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/monthWiseCourseAttendance")
    public ResponseEntity<Map<String, Object>> fetchMonthwiseAttendanceForCourse(
            @RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap();
        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);
        if (tokenMap.get("valid") == (Boolean) false) {
            returnObject.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObject.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
        Integer courseId = (Integer) map.get("courseId");

        try {
            returnObject.put("data", statisticsService.fetchMonthWiseAttendanceCountForCourse(courseId));
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", "Internal Error");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/courseWiseAttendanceOfStudentInAMonth")
    public ResponseEntity<Map<String, Object>> fetchCourseWiseAttendanceOfStudentInAMonth(
            @RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap();
        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);
        if (tokenMap.get("valid") == (Boolean) false) {
            returnObject.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        } else if (!tokenMap.get("role").equals("admin")) {
            returnObject.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
        Integer studentId = (Integer) map.get("studentId");
        Integer monthNumber = (Integer) map.get("monthNumber");
        try {
            returnObject.put("data",
                    statisticsService.fetchCourseWiseAttendanceOfStudentInAMonth(studentId, monthNumber));
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", "Internal Error");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}