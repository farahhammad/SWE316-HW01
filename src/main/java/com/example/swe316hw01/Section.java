package com.example.swe316hw01;

/**
 * Represents a course section (concrete class)
 * Inherits from Course (which inherits from Department)
 * Single Responsibility: Manages section-specific information
 */
public class Section extends Course {
    private String crn;
    private String sectionNumber;
    private String type;           // e.g., "LEC", "LAB"
    private Meeting meeting;
    private String instructor;

    public Section(String departmentName, String courseTitle, String courseName,
                   String crn, String sectionNumber,
                   String type, Meeting meeting, String instructor) {
        super(departmentName, courseTitle, courseName);
        this.crn = crn;
        this.sectionNumber = sectionNumber;
        this.type = type;
        this.meeting = meeting;
        this.instructor = instructor;
    }

    public String getCrn() {
        return crn;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public String getType() {
        return type;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public String getInstructor() {
        return instructor;
    }

    // Check if section meets on a specific day
    public boolean meetsOnDay(String day) {
        return meeting.isOnDay(day);
    }

    @Override
    public String toString() {
        return crn + " - " + getCourseTitle() + ": " + getCourseName() +
                " (" + type + ") - " + getDepartmentName();
    }
}