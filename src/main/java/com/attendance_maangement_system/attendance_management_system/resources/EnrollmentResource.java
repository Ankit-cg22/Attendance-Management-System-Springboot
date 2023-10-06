package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.domain.Enrollment;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.services.EnrollmentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentResource {
    @Autowired
    EnrollmentService enrollmentService;

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<Map<String, Object>> fetchEnrollmentById(HttpServletRequest request,
            @PathVariable("enrollmentId") Integer enrollmentId) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            Enrollment enrollment = enrollmentService.fetchById(enrollmentId);
            returnObject.put("data", enrollment);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/enrollStudent")
    public ResponseEntity<Map<String, Object>> enrollStudent(HttpServletRequest request,
            @RequestBody Map<String, Object> map)
            throws InvalidRequestException {
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
            Integer courseId = (Integer) map.get("courseId");
            Integer studentId = (Integer) map.get("studentId");
            Enrollment newEnrollment = enrollmentService.enrollStudent(courseId, studentId);
            returnObject.put("data", newEnrollment);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/evictStudent")
    public ResponseEntity<Map<String, Object>> evictStudent(HttpServletRequest request,
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
            Integer courseId = (Integer) map.get("courseId");
            Integer studentId = (Integer) map.get("studentId");
            enrollmentService.evictStudent(courseId, studentId);
            returnObject.put("success", true);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

}
