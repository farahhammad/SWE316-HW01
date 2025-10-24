package com.example.swe316hw01;

/**
 * A catalog course belonging to a department.
 * Example: courseCode="ICS 344", courseTitle="Computer Networks".
 */
public class Course {
    private final String courseCode;   // e.g., "ICS 344"
    private final String courseTitle;  // e.g., "Computer Networks"
    private final Department department;

    public Course(String courseCode, String courseTitle, Department department) {
        this.courseCode = (courseCode == null || courseCode.isBlank()) ? "N/A" : courseCode;
        this.courseTitle = (courseTitle == null || courseTitle.isBlank()) ? "N/A" : courseTitle;
        this.department = (department == null) ? new Department("Unknown") : department;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return courseCode + ": " + courseTitle + " - " + department;
    }
}
