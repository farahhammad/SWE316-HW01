package com.example.swe316hw01;

/** Plain entity representing an academic department. */
public class Department {
    private final String departmentName;

    public Department(String departmentName) {
        this.departmentName = (departmentName == null || departmentName.isBlank()) ? "Unknown" : departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return departmentName;
    }
}
