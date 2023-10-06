package com.attendance_maangement_system.attendance_management_system.services;

import com.attendance_maangement_system.attendance_management_system.domain.Enrollment;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface EnrollmentService {
    Enrollment fetchById(Integer enrollmentId) throws ResourceNotFoundException;

    Enrollment enrollStudent(Integer courseId, Integer studentId) throws InvalidRequestException;

    void evictStudent(Integer courseId, Integer studentId) throws InvalidRequestException;
}
