package com.attendance_maangement_system.attendance_management_system.services;

import java.util.List;

import com.attendance_maangement_system.attendance_management_system.domain.User;

public interface AdminService {
    void makeAdmin(Integer userId);

    List<User> fetchAdminRequests();
}
