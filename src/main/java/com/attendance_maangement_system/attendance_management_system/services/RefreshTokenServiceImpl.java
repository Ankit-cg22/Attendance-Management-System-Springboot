package com.attendance_maangement_system.attendance_management_system.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.Constants;
import com.attendance_maangement_system.attendance_management_system.domain.RefreshToken;
import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.repository.RefreshTokenRepository;
import com.attendance_maangement_system.attendance_management_system.repository.UserRepository;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(Integer userId) {
        refreshTokenRepository.deleteByUserId(userId); // if any old refresh token exists , delete it and create new one
        // the new refresh token will have 24 hrs from time of this new login
        String refreshToken = UUID.randomUUID().toString();
        Timestamp expiry = Timestamp.from(Instant.now().plusMillis(Constants.REFRESH_TOKEN_VALIDITY));
        RefreshToken refreshTokenObj = new RefreshToken(refreshToken, expiry, userId);
        refreshTokenRepository.createRefreshToken(refreshTokenObj);
        return refreshTokenObj;
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken) {
        try {
            RefreshToken refreshTokenOb = refreshTokenRepository.findById(refreshToken);
            if (refreshTokenOb.getExpiry().compareTo(Timestamp.from(Instant.now())) < 0) {
                refreshTokenRepository.delete(refreshToken);
                throw new InvalidRequestException("Refresh Token has expired.");
            }
            return refreshTokenOb;
        } catch (Exception e) {
            throw e;
        }
    }

}
