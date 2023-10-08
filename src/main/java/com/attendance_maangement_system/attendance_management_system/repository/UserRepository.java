package com.attendance_maangement_system.attendance_management_system.repository;

import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.exceptions.AuthException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface UserRepository {
    Integer create(String firstName, String lastName, String email, String password, String role) throws AuthException;
    // create user and returns the generated user id

    User findByEmailAndPassword(String email, String password) throws AuthException;
    // find user using the provided email and password

    User findByUserId(Integer userId) throws ResourceNotFoundException;

    void updateUser(Integer userId, User user) throws ResourceNotFoundException;

}
