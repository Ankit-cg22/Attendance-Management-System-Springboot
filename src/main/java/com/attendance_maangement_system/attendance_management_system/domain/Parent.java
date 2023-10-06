package com.attendance_maangement_system.attendance_management_system.domain;

public class Parent {
    private int parentId, childId;
    private String firstName, lastName, email;

    public Parent(int parentId, int childId, String firstName, String lastName, String email) {
        this.parentId = parentId;
        this.childId = childId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
