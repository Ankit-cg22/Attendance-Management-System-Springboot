package com.attendance_maangement_system.attendance_management_system.services;

import java.util.List;

import com.attendance_maangement_system.attendance_management_system.domain.Course;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface CourseService {
    List<Course> fetchAllCourses() throws RuntimeException;

    Course fectchCourseById(Integer courseId) throws ResourceNotFoundException;

    Course addCourse(String courseTitle) throws InvalidRequestException;

    void removeCourseById(Integer courseId) throws InvalidRequestException;

    void updateCourse(Integer courseId, Course course) throws InvalidRequestException;

    List<Integer> fetchEnrolledStudents(Integer courseId) throws ResourceNotFoundException;
}
