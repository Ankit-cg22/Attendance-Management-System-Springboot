package com.attendance_maangement_system.attendance_management_system.repository;

import java.sql.Date;

import com.attendance_maangement_system.attendance_management_system.domain.Enrollment;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface EnrollmentRepository {
    Enrollment findById(Integer enrollmentId) throws ResourceNotFoundException;

    Integer create(Integer courseId, Integer studentId, Date enrollmentDate) throws InvalidRequestException;

    void delete(Integer courseId, Integer studentId) throws InvalidRequestException;
}
