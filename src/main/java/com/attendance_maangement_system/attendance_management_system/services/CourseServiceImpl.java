package com.attendance_maangement_system.attendance_management_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.domain.Course;
import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;
import com.attendance_maangement_system.attendance_management_system.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Override
    public List<Course> fetchAllCourses() throws RuntimeException {
        try {
            return courseRepository.fetchAll();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Course fectchCourseById(Integer courseId) throws ResourceNotFoundException {
        try {
            return courseRepository.findById(courseId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Course addCourse(String courseTitle) throws InvalidRequestException {
        try {
            int courseId = courseRepository.create(courseTitle);
            return courseRepository.findById(courseId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void removeCourseById(Integer courseId) throws InvalidRequestException {
        try {
            courseRepository.removeById(courseId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void updateCourse(Integer courseId, Course course) throws InvalidRequestException {
        try {
            courseRepository.update(courseId, course);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Student> fetchEnrolledStudents(Integer courseId) throws ResourceNotFoundException {
        try {
            return courseRepository.findEnrolledStudents(courseId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Student> fetchNotEnrolledStudents(Integer courseId) throws ResourceNotFoundException {
        try {
            return courseRepository.findNotEnrolledStudents(courseId);
        } catch (Exception e) {
            throw e;
        }
    }

}
