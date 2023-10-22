package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.coyote.Response;
import org.apache.tomcat.util.bcel.Const;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.domain.Parent;
import com.attendance_maangement_system.attendance_management_system.domain.RefreshToken;
import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.services.ParentService;
import com.attendance_maangement_system.attendance_management_system.services.RefreshTokenService;
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

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, Object> userMap) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            String firstName = (String) userMap.get("firstName");
            String lastName = (String) userMap.get("lastName");
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");
            String role = (String) userMap.get("role");
            Integer childId = (Integer) userMap.get("childId");
            CompletableFuture<User> user1 = userService.registerUser(firstName, lastName, email, password, role);
            User user = user1.join();
            if (role.equals("student")) {
                Student newStudent = studentService.addStudent(user.getUserId());
                returnObject.put("studentId", newStudent.getStudentId());
                returnObject.put("token", generateJWTToken(user, (Student) returnObject.get("student")));
            } else if (role.equals("parent")) {
                System.out.println(childId);
                System.out.println(user.getUserId());
                Parent newParent = parentService.addParent(user.getUserId(), childId);
                returnObject.put("parentId", newParent.getParentId());
                returnObject.put("childId", newParent.getChildId());
                returnObject.put("token", generateJWTToken(user, (Parent) returnObject.get("parent")));
            }
            returnObject.put("user", user);

            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, Object> userMap) {
        Map<String, Object> returnObject = new HashMap<>();

        try {
            String email = (String) userMap.get("email");
            String password = (String) userMap.get("password");

            // using the received data we check if provided data represents a valid user
            User user = userService.validateUser(email, password);
            returnObject.put("user", user);
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

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserId());
            returnObject.put("refreshToken", refreshToken.getRefreshToken());

            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable("userId") Integer userId,
            @RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap<>();
        try {
            String token = (String) map.get("token");
            Map<String, Object> tokenMap = Constants.validateToken(token);
            if (tokenMap.get("valid") == (Boolean) false) {
                returnObject.put("error", "invalid token");
                return new ResponseEntity<>(returnObject, HttpStatus.BAD_REQUEST);
            } else if (tokenMap.get("userId") != userId) {
                returnObject.put("error", "unauthorized access");
                return new ResponseEntity<>(returnObject, HttpStatus.BAD_REQUEST);
            }
            String firstName = (String) map.get("firstName");
            String lastName = (String) map.get("lastName");
            String email = (String) map.get("email");
            String password = (String) map.get("password");
            String role = (String) tokenMap.get("role");

            User user = new User(userId, firstName, lastName, email, password, role);
            User updatedUser = userService.updateUser(userId, user);

            returnObject.put("success", true);
            returnObject.put("user", updatedUser);

            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
    }

    // @GetMapping("/adminRequests")

    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, Object>> refreshJWTToken(@RequestBody Map<String, Object> map) {
        Map<String, Object> returnObject = new HashMap();
        String refreshToken = (String) map.get("refreshToken");
        try {
            RefreshToken refreshTokenObj = refreshTokenService.verifyRefreshToken(refreshToken);
            User user = userService.getUser(refreshTokenObj.getUserId());
            returnObject.put("user", user);
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
            returnObject.put("refreshToken", refreshTokenObj.getRefreshToken());
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
        } catch (Exception e) {
            returnObject.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);
        }
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
                .setExpiration(new Date(timestamp + Constants.API_TOKEN_VALIDITY))
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
