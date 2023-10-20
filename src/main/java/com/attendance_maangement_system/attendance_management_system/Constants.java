package com.attendance_maangement_system.attendance_management_system;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.security.Keys;

public class Constants {
    // public static final String API_SECRET_KEY =
    // "attendancemanagementJWTKEY22attendancemanagementJWTKEY";
    public static final byte[] API_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
    // token validity time = 30 min , we store that in millisecond
    public static final long API_TOKEN_VALIDITY = 30 * 60 * 1000;
    // refresh token is valid for a day
    public static final long REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> map = new HashMap<>();

        String[] chunks = token.split("\\.");
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        SignatureAlgorithm sa = SignatureAlgorithm.HS256;

        SecretKeySpec secretKeySpec = new SecretKeySpec(Constants.API_SECRET_KEY, sa.getJcaName());
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
        if (!validator.isValid(tokenWithoutSignature, signature)) {
            map.put("valid", false);
            return map;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(API_SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            long expirationTime = claims.getExpiration().getTime(); // in milliseconds
            long currentTime = System.currentTimeMillis(); // current time in milliseconds
            if (expirationTime < currentTime) {
                // jwt has expired
                map.put("valid", false);
                return map;
            }
        } catch (Exception e) {
            map.put("valid", false);
            return map;
        }

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        map.put("valid", true);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JWT claims string into a JsonNode
            JsonNode claimsNode = objectMapper.readTree(payload);

            // Access individual properties from the JsonNode
            int userId = claimsNode.get("userId").asInt();
            String email = claimsNode.get("email").asText();
            String firstName = claimsNode.get("firstName").asText();
            String lastName = claimsNode.get("lastName").asText();
            String role = claimsNode.get("role").asText();

            map.put("userId", userId);
            map.put("email", email);
            map.put("firstName", firstName);
            map.put("lastName", lastName);
            map.put("role", role);

            if (claimsNode.has("studentId")) {
                map.put("studentId", claimsNode.get("studentId").asInt());
            }
            if (claimsNode.has("childId")) {
                map.put("childId", claimsNode.get("childId").asInt());
            }

        } catch (Exception e) {

        }

        return map;
    }

}
