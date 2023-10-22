package com.attendance_maangement_system.attendance_management_system.services;

import java.util.concurrent.CompletableFuture;

import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.exceptions.AuthException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface UserService {
    CompletableFuture<User> registerUser(String firstName, String lastName, String email, String password, String role)
            throws AuthException;

    User validateUser(String email, String password) throws AuthException;

    User updateUser(Integer userId, User user) throws ResourceNotFoundException;

    User getUser(Integer userId);

}
