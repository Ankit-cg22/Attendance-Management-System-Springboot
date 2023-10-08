package com.attendance_maangement_system.attendance_management_system.repository;

import com.attendance_maangement_system.attendance_management_system.domain.Parent;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface ParentRepository {
    Integer create(Integer userId, Integer childId)
            throws InvalidRequestException;

    Parent findById(Integer parentId) throws ResourceNotFoundException;

    Integer getParentIdFromUserId(Integer userId) throws ResourceNotFoundException;
}
