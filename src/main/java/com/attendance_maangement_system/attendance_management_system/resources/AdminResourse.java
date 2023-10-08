package com.attendance_maangement_system.attendance_management_system.resources;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.services.AdminService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

@RestController
@RequestMapping("/api/admin")
public class AdminResourse {
    @Autowired
    AdminService adminService;

    @GetMapping("/makeAdmin/{userId}")
    public ResponseEntity<Map<String, Object>> makeAdmin(HttpServletRequest request,
            @PathVariable("userId") Integer userId, @RequestBody Map<String, Object> map) {

        Map<String, Object> returnObject = new HashMap<>();
        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObject.put("error", "invalid token");
        } else if (tokenMap.get("role").equals("admin")) {
            adminService.makeAdmin(userId);
            returnObject.put("succes", true);
        } else {
            returnObject.put("error", "unauthorized access");
        }

        return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
    }

    @GetMapping("/adminRequests")
    public ResponseEntity<Map<String, Object>> fetchAdminRequests(@RequestBody Map<String, Object> map) {
        String token = (String) map.get("token");
        Map<String, Object> tokenMap = Constants.validateToken(token);
        Map<String, Object> returnObject = new HashMap<>();

        if (tokenMap.get("valid") == (Boolean) false) {
            returnObject.put("error", "invalid token");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);

        } else if (!tokenMap.get("role").equals("admin")) {
            returnObject.put("error", "unauthorized access");
            return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.BAD_REQUEST);

        }
        List<User> userList = adminService.fetchAdminRequests();
        returnObject.put("adminRequests", userList);
        return new ResponseEntity<Map<String, Object>>(returnObject, HttpStatus.OK);
    }

}
