package com.attendance_maangement_system.attendance_management_system.repository;

import java.util.List;

import com.attendance_maangement_system.attendance_management_system.domain.User;

public interface AdminRepository {
    void makeAdmin(Integer userId);

    List<User> fetchAdminRequests();
}
