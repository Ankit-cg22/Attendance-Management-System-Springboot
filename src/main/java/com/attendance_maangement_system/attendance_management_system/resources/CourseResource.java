package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.HashMap;
import java.util.List;
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
import com.attendance_maangement_system.attendance_management_system.domain.Course;
import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.services.CourseService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/course")
public class CourseResource {
    @Autowired
    CourseService courseService;

    @GetMapping("/allCourses")
    public ResponseEntity<Map<String, Object>> getAllCourses(HttpServletRequest request) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            List<Course> courseList = courseService.fetchAllCourses();
            returnObject.put("data", courseList);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", "Internal server error.");
            return new ResponseEntity<>(returnObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> getcourseById(HttpServletRequest request,
            @PathVariable Integer courseId) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            Course course = courseService.fectchCourseById(courseId);
            returnObject.put("data", course);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> addCourse(HttpServletRequest request,
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
            String courseTitle = (String) map.get("courseTitle");
            Course newCourse = courseService.addCourse(courseTitle);
            returnObject.put("data", newCourse);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateCourse/{courseId}")
    public ResponseEntity<Map<String, Object>> updateCourse(HttpServletRequest request,
            @PathVariable("courseId") Integer courseId, @RequestBody Map<String, Object> map) {

        Map<String, Object> returnObject = new HashMap<>();
        Course course = new Course(courseId, (String) ((Map<String, Object>) map.get("course")).get("courseTitle"));
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
            courseService.updateCourse(courseId, course);
            returnObject.put("success", true);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> deleteCourse(HttpServletRequest request,
            @PathVariable("courseId") Integer courseId, @RequestBody Map<String, Object> map) {
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
            courseService.removeCourseById(courseId);
            returnObject.put("success", true);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/enrolledStudents/{courseId}")
    public ResponseEntity<Map<String, Object>> fetchEnrolledStudents(HttpServletRequest request,
            @PathVariable("courseId") Integer courseId) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            List<Student> studentList = courseService.fetchEnrolledStudents(courseId);
            returnObject.put("data", studentList);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/notEnrolledStudents/{courseId}")
    public ResponseEntity<Map<String, Object>> fetchNotEnrolledStudents(HttpServletRequest request,
            @PathVariable("courseId") Integer courseId) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            List<Student> studentList = courseService.fetchNotEnrolledStudents(courseId);
            returnObject.put("data", studentList);
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            returnObject.put("error", errorMessage);
            return new ResponseEntity<>(returnObject, HttpStatus.NOT_FOUND);
        }
    }
}
