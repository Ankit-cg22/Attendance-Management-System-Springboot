package com.attendance_maangement_system.attendance_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository;

    @Override
    public void makeAdmin(Integer userId) {
        adminRepository.makeAdmin(userId);
    }
}
