package com.attendance_maangement_system.attendance_management_system.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.domain.Enrollment;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;
import com.attendance_maangement_system.attendance_management_system.repository.EnrollmentRepository;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment fetchById(Integer enrollmentId) throws ResourceNotFoundException {
        try {
            return enrollmentRepository.findById(enrollmentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Enrollment enrollStudent(Integer courseId, Integer studentId) throws InvalidRequestException {
        try {
            Date date = new java.sql.Date(System.currentTimeMillis());
            int enrollmentId = enrollmentRepository.create(courseId, studentId, date);
            return enrollmentRepository.findById(enrollmentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void evictStudent(Integer courseId, Integer studentId) throws InvalidRequestException {
        try {
            enrollmentRepository.delete(courseId, studentId);
        } catch (Exception e) {
            throw e;
        }
    }

}
