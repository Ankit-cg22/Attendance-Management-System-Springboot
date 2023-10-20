package com.attendance_maangement_system.attendance_management_system.domain;

import java.sql.Timestamp;
import java.time.Instant;

public class RefreshToken {
    private String refreshToken;
    private Timestamp expiry;
    private Integer userId;

    public RefreshToken(String refreshToken, Timestamp expiry, Integer userId) {
        this.refreshToken = refreshToken;
        this.expiry = expiry;
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Timestamp getExpiry() {
        return expiry;
    }

    public void setExpiry(Timestamp expiry) {
        this.expiry = expiry;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}