package com.attendance_maangement_system.attendance_management_system.services;

import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.exceptions.AuthException;

public interface UserService {
    User registerUser(String firstName, String lastName, String email, String password, String role)
            throws AuthException;

    User validateUser(String email, String password) throws AuthException;

}
