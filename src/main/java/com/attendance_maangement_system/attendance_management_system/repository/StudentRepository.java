package com.attendance_maangement_system.attendance_management_system.repository;

import java.util.List;

import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface StudentRepository {
    List<Student> findAll();

    Student findById(Integer studentId) throws ResourceNotFoundException;

    Integer create(Integer userId) throws InvalidRequestException;

    void removeById(Integer studentId) throws InvalidRequestException;

    // void update(Integer studentId) throws InvalidRequestException;

    List<Integer> findEnrolledCourses(Integer studentId) throws ResourceNotFoundException;

    Integer getStudentIdFromUserId(Integer userId) throws ResourceNotFoundException;
}
