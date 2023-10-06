package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.domain.Parent;
import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.services.ParentService;
import com.attendance_maangement_system.attendance_management_system.services.StudentService;
import com.attendance_maangement_system.attendance_management_system.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/user")
public class UserResource {
    @Autowired
    UserService userService;

    @Autowired
    StudentService studentService;

    @Autowired
    ParentService parentService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String role = (String) userMap.get("role");
        Integer childId = (Integer) userMap.get("childId");
        User user = userService.registerUser(firstName, lastName, email, password, role);

        Map<String, Object> returnObject = new HashMap<>();

        if (role.equals("student")) {
            Student newStudent = studentService.addStudent(user.getUserId(), firstName, lastName, email);
            returnObject.put("student", newStudent);
            returnObject.put("token", generateJWTToken(user, (Student) returnObject.get("student")));
        } else if (role.equals("parent")) {
            System.out.println(childId);
            System.out.println(user.getUserId());
            Parent newParent = parentService.addParent(user.getUserId(), childId, firstName, lastName, email);
            returnObject.put("parent", newParent);
            returnObject.put("childId", newParent.getChildId());
            returnObject.put("token", generateJWTToken(user, (Parent) returnObject.get("parent")));
        }

        Map<String, Object> user1 = new HashMap<>();
        user1.put("userId", user.getUserId());
        user1.put("firstName", user.getFirstName());
        user1.put("lastName", user.getLastName());
        user1.put("email", user.getEmail());
        user1.put("role", user.getRole());
        returnObject.put("user", user1);

        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, Object> userMap) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        // using the received data we check if provided data represents a valid user
        User user = userService.validateUser(email, password);
        Map<String, Object> returnObject = new HashMap<>();
        Map<String, Object> user1 = new HashMap<>();
        user1.put("userId", user.getUserId());
        user1.put("firstName", user.getFirstName());
        user1.put("lastName", user.getLastName());
        user1.put("email", user.getEmail());
        user1.put("role", user.getRole());
        returnObject.put("user", user1);
        Object obj = null;

        if (user.getRole().equals("student")) {
            Integer studentId = studentService.getStudentIdFromUserId(user.getUserId());
            returnObject.put("studentId", studentId);
            obj = studentService.fetchStudentById(studentId);
        }
        if (user.getRole().equals("parent")) {
            Integer parentId = parentService.getParentIdFromUserId(user.getUserId());
            returnObject.put("parentId", parentId);
            obj = parentService.fetchParentById(parentId);
            returnObject.put("childId", ((Parent) obj).getChildId());
        }
        returnObject.put("token", generateJWTToken(user, obj));
        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }

    private String generateJWTToken(User user, Object obj) {
        long timestamp = System.currentTimeMillis();
        // we use current time to set the expirty of the token

        String key = "null";
        Integer val = 0;
        if (obj instanceof Student) {
            key = "studentId";
            val = ((Student) obj).getStudentId();
        }
        if (obj instanceof Parent) {
            key = "childId";
            val = ((Parent) obj).getChildId();
        }
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(Constants.API_SECRET_KEY), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + 30 * 60 * 1000))
                .claim("userId", user.getUserId())
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("role", user.getRole())
                .claim(key, val)
                .compact();

        return token;
    }

}
