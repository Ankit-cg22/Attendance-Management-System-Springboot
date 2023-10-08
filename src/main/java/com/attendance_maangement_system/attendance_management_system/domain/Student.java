package com.attendance_maangement_system.attendance_management_system.domain;

public class Student extends User {
    private int studentId;

    public Student(Integer userId, String firstName, String lastName, String email, String password, String role,
            int studentId) {
        super(userId, firstName, lastName, email, password, role);
        this.studentId = studentId;
    }

    public Student(Integer userId, String firstName, String lastName, String email, String role,
            int studentId) {
        super(userId, firstName, lastName, email, role);
        this.studentId = studentId;
    }

    public Student(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

}
