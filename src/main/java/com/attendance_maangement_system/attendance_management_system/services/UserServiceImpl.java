package com.attendance_maangement_system.attendance_management_system.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.domain.User;
import com.attendance_maangement_system.attendance_management_system.exceptions.AuthException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;
import com.attendance_maangement_system.attendance_management_system.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    // @Async
    @Override
    public User registerUser(String firstName, String lastName, String email, String password, String role)
            throws AuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        if (email != null)
            email = email.toLowerCase();
        // convert email to lower case

        if (!pattern.matcher(email).matches()) {
            throw new AuthException("Invalid email format");
        }
        // if email does not match email pattern , throw error

        try {
            Integer userId = userRepository.create(firstName, lastName, email, password, role);
            System.out.println("THIS IS USER ID : " + userId);
            return userRepository.findByUserId(userId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public User validateUser(String email, String password) throws AuthException {
        if (email != null)
            email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User updateUser(Integer userId, User user) throws ResourceNotFoundException {
        try {
            userRepository.updateUser(userId, user);
            return userRepository.findByUserId(userId);
        } catch (Exception e) {
            throw e;
        }
    }

}
