package com.attendance_maangement_system.attendance_management_system.services;

import java.util.List;
import java.util.Map;

import org.apache.catalina.util.ResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance_maangement_system.attendance_management_system.domain.Student;
import com.attendance_maangement_system.attendance_management_system.exceptions.InvalidRequestException;
import com.attendance_maangement_system.attendance_management_system.exceptions.ResourceNotFoundException;
import com.attendance_maangement_system.attendance_management_system.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public List<Student> fetchAllStudents() throws RuntimeException {
        try {
            return studentRepository.findAll();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Student fetchStudentById(Integer studentId) throws ResourceNotFoundException {
        try {
            return studentRepository.findById(studentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Student addStudent(Integer userId)
            throws InvalidRequestException {
        try {
            int studentId = studentRepository.create(userId);
            return studentRepository.findById(studentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void removeStudentById(Integer studentId) throws InvalidRequestException {
        try {
            studentRepository.removeById(studentId);
        } catch (Exception e) {
            throw e;
        }
    }

    // @Override
    // public void updateStudent(Integer studentId, Student student) throws
    // ResourceNotFoundException {
    // try {
    // studentRepository.update(studentId, student);
    // } catch (Exception e) {
    // throw e;
    // }
    // }

    @Override
    public List<Map<String, Object>> fetchEnrolledCourses(Integer studentId) throws ResourceNotFoundException {
        try {
            return studentRepository.findEnrolledCourses(studentId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Integer getStudentIdFromUserId(Integer userId) throws ResourceNotFoundException {
        try {
            return studentRepository.getStudentIdFromUserId(userId);
        } catch (Exception e) {
            throw e;
        }
    }
}
