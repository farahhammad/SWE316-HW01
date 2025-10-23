package com.example.swe316hw01;

public abstract class Course extends Department {
    private final String courseTitle;  // e.g., "SWE 316"
    private final String courseName;   // e.g., "Software Design and Construction"

    public Course(String departmentName, String courseTitle, String courseName) {
        super(departmentName);
        this.courseTitle = courseTitle;
        this.courseName = courseName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public String toString() {
        return courseTitle + ": " + courseName;
    }
}