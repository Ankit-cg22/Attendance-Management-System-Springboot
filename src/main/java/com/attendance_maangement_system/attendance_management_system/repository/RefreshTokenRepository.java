package com.attendance_maangement_system.attendance_management_system.repository;

import com.attendance_maangement_system.attendance_management_system.domain.RefreshToken;

public interface RefreshTokenRepository {
    RefreshToken createRefreshToken(RefreshToken refreshTokenObj);

    RefreshToken findById(String refreshToken);

    void delete(String refreshToken);

    void deleteByUserId(Integer userId);
}
