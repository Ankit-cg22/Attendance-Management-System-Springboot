package com.attendance_maangement_system.attendance_management_system.services;

import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

import java.util.List;

public interface StudentService {
    List<Student> fetchAllStudents() throws RuntimeException;

    Student fetchStudentById(Integer studentId) throws ResourceNotFoundException;

    Student addStudent(Integer userId, String firstName, String lastName, String email) throws InvalidRequestException;

    void removeStudentById(Integer studentId) throws InvalidRequestException;

    void updateStudent(Integer studentId, Student student) throws ResourceNotFoundException;

    List<Integer> fetchEnrolledCourses(Integer studentId) throws ResourceNotFoundException;

    Integer getStudentIdFromUserId(Integer userId) throws ResourceNotFoundException;
}
