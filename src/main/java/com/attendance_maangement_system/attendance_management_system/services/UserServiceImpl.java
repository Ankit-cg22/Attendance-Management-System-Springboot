package com.attendance_maangement_system.attendance_management_system.services;

import java.util.concurrent.CompletableFuture;
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

    @Async
    @Override
    public CompletableFuture<User> registerUser(String firstName, String lastName, String email, String password,
            String role)
            throws AuthException {
        long start = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();

        Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        // System.out.println(threadName + " : long loop");
        // for (int i = 0; i < 1000; i++) {
        // // empty loop
        // };

        System.out.println(threadName + " : mail pattern check");
        if (email != null)
            email = email.toLowerCase();
        // convert email to lower case

        System.out.println(threadName + " : mail duplicate check");
        if (!pattern.matcher(email).matches()) {
            throw new AuthException("Invalid email format");
        }
        // if email does not match email pattern , throw error

        System.out.println(threadName + " : user creation");
        try {
            Integer userId = userRepository.create(firstName, lastName, email, password, role);
            System.out.println("THIS IS USER ID : " + userId);
            User user = userRepository.findByUserId(userId);

            long end = System.currentTimeMillis();
            System.out.println("Thread: " + threadName);
            System.out.println("Time taken : " + (end - start) + " milliseconds");

            return CompletableFuture.completedFuture(user);
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

    @Override
    public User getUser(Integer userId) {
        try {
            return userRepository.findByUserId(userId);
        } catch (Exception e) {
            throw e;
        }
    }

}
