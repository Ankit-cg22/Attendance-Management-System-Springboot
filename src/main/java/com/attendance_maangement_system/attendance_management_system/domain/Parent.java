package com.attendance_maangement_system.attendance_management_system.domain;

public class Parent extends User {
    private int parentId, childId;

    public int getParentId() {
        return parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public Parent(Integer userId, String firstName, String lastName, String email, String password, String role,
            int parentId, int childId) {
        super(userId, firstName, lastName, email, password, role);
        this.parentId = parentId;
        this.childId = childId;
    }

    public Parent(Integer userId, String firstName, String lastName, String email, String role,
            int parentId, int childId) {
        super(userId, firstName, lastName, email, role);
        this.parentId = parentId;
        this.childId = childId;
    }

    public Parent(int parentId) {
        this.parentId = parentId;
    }

    public int getStudentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

}
