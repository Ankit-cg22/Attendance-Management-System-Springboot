package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.domain.Course;
import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.services.StudentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/student")
public class StudentResource {
    @Autowired
    StudentService studentService;

    @GetMapping("/allStudents") // GET /api/student/allStudents
    public ResponseEntity<Map<String, Object>> getAllStdudents(HttpServletRequest request) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            List<Student> studentList = studentService.fetchAllStudents();
            returnObject.put("data", studentList);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", "Internal server error.");
            return new ResponseEntity<>(returnObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{studentId}") // GET /api/student/{studentId}
    public ResponseEntity<Map<String, Object>> getStudentById(HttpServletRequest request,
            @PathVariable Integer studentId) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            Student student = studentService.fetchStudentById(studentId);
            returnObject.put("data", student);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

    // @PostMapping("/create") // POST /api/student/create/
    // public ResponseEntity<Map<String, Object>> addStudent(HttpServletRequest
    // request,
    // @RequestBody Map<String, Object> map) {
    // Map<String, Object> returnObject = new HashMap<>();
    // String token = (String) map.get("token");
    // Map<String, Object> tokenMap = Constants.validateToken(token);

    // if (tokenMap.get("valid") == (Boolean) false) {
    // returnObject.put("error", "invalid token");
    // return new ResponseEntity<Map<String, Object>>(returnObject,
    // HttpStatus.BAD_REQUEST);
    // } else if (!tokenMap.get("role").equals("admin")) {
    // returnObject.put("error", "unauthorized access");
    // return new ResponseEntity<Map<String, Object>>(returnObject,
    // HttpStatus.BAD_REQUEST);
    // }
    // try {
    // String firstName = (String) map.get("firstName");
    // String lastName = (String) map.get("lastName");
    // String email = (String) map.get("email");
    // Student newStudent = studentService.addStudent(firstName, lastName, email);
    // returnObject.put("data", newStudent);
    // return new ResponseEntity<>(returnObject, HttpStatus.OK);
    // } catch (Exception e) {
    // String errorMessage = e.getMessage();
    // returnObject.put("error", errorMessage);
    // return new ResponseEntity<>(returnObject, HttpStatus.BAD_REQUEST);
    // }
    // }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Map<String, Object>> deleteStudent(HttpServletRequest request,
            @PathVariable("studentId") Integer studentId, @RequestBody Map<String, Object> map) {

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
            studentService.removeStudentById(studentId);
            returnObject.put("success", true);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }

    // @PostMapping("/{studentId}")
    // public ResponseEntity<Map<String, Object>> updateStudent(HttpServletRequest
    // request,
    // @PathVariable("studentId") Integer studentId, @RequestBody Map<String,
    // Object> map) {

    // Map<String, Object> returnObject = new HashMap<>();
    // String token = (String) map.get("token");
    // Map<String, Object> tokenMap = Constants.validateToken(token);

    // if (tokenMap.get("valid") == (Boolean) false) {
    // returnObject.put("error", "invalid token");
    // return new ResponseEntity<Map<String, Object>>(returnObject,
    // HttpStatus.BAD_REQUEST);
    // } else if ((Integer) tokenMap.get("studentId") != studentId) {
    // returnObject.put("error", "unauthorized access");
    // return new ResponseEntity<Map<String, Object>>(returnObject,
    // HttpStatus.BAD_REQUEST);
    // }
    // try {
    // Student student = new Student(studentId, (String) map.get("firstName"),
    // (String) map.get("lastName"),
    // (String) map.get("email"));
    // studentService.updateStudent(studentId, student);
    // returnObject.put("success", true);
    // return new ResponseEntity<>(returnObject, HttpStatus.OK);
    // } catch (Exception e) {
    // String errorMessage = e.getMessage();
    // returnObject.put("error", errorMessage);
    // return new ResponseEntity<>(returnObject, HttpStatus.BAD_REQUEST);
    // }
    // }

    @GetMapping("/enrolledCourses/{studentId}")
    public ResponseEntity<Map<String, Object>> getEnrolledCourses(HttpServletRequest request,
            @PathVariable("studentId") Integer studentId) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            List<Integer> courseIdList = studentService.fetchEnrolledCourses(studentId);
            returnObject.put("data", courseIdList);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

}
