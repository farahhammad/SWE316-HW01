package com.example.swe316hw01;


public abstract class Department {
    private final String departmentName;

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return departmentName;
    }
}