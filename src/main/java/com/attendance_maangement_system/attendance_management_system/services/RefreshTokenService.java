package com.attendance_maangement_system.attendance_management_system.services;

import com.attendance_maangement_system.attendance_management_system.domain.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Integer userId);

    RefreshToken verifyRefreshToken(String refreshToken);
}
