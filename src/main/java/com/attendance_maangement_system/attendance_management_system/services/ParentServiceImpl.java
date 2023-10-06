package com.attendance_maangement_system.attendance_management_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.domain.Parent;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;
import com.attendance_maangement_system.attendance_management_system.repository.ParentRepository;

@Service
public class ParentServiceImpl implements ParentService {
    @Autowired
    ParentRepository parentRepository;

    @Override
    public Parent fetchParentById(Integer parentId) throws ResourceNotFoundException {
        try {
            return parentRepository.findById(parentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Parent addParent(Integer userId, Integer childId, String firstName, String lastName, String email)
            throws InvalidRequestException {
        try {
            int parentId = parentRepository.create(userId, childId, firstName, lastName, email);
            return parentRepository.findById(parentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Integer getParentIdFromUserId(Integer userId) throws ResourceNotFoundException {
        try {
            return parentRepository.getParentIdFromUserId(userId);
        } catch (Exception e) {
            throw e;
        }
    }

}
