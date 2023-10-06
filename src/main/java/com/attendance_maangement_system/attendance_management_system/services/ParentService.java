package com.attendance_maangement_system.attendance_management_system.services;

import com.attendance_maangement_system.attendance_management_system.domain.Parent;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;

public interface ParentService {
    Parent fetchParentById(Integer parentId) throws ResourceNotFoundException;

    Parent addParent(Integer userId, Integer childId, String firstName, String lastName, String email)
            throws InvalidRequestException;

    Integer getParentIdFromUserId(Integer userId) throws ResourceNotFoundException;

}
