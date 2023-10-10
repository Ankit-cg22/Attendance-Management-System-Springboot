package com.attendance_maangement_system.attendance_management_system.repository;

import java.util.List;

import com.attendance_maangement_system.attendance_management_system.domain.Course;
import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface CourseRepository {
    List<Course> fetchAll();

    Course findById(Integer courseId) throws ResourceNotFoundException;

    Integer create(String courseTitle) throws InvalidRequestException;

    void removeById(Integer studentId) throws InvalidRequestException;

    void update(Integer courseId, Course course) throws InvalidRequestException;

    List<Student> findEnrolledStudents(Integer courseId) throws ResourceNotFoundException;

    List<Student> findNotEnrolledStudents(Integer courseId) throws ResourceNotFoundException;
}
