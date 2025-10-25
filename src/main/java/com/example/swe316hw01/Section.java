package com.example.swe316hw01;

/**
 * Represents an offering of a course in a given term (CRN, section number, meeting, etc.)
 */
public class Section {
    private final String crn;
    private final String sectionNumber;
    private final String type;       // e.g., "LEC", "LAB"
    private final Meeting meeting;   // may be null if TBA
    private final String instructor; // "TBA" if unknown
    private final Course course;     // never null

    public Section(String crn,
                   String sectionNumber,
                   String type,
                   Meeting meeting,
                   String instructor,
                   Course course) {
        this.crn = (crn == null) ? "" : crn.trim();
        this.sectionNumber = (sectionNumber == null || sectionNumber.isBlank()) ? "N/A" : sectionNumber.trim();
        this.type = (type == null || type.isBlank()) ? "LEC" : type.trim().toUpperCase();
        this.meeting = meeting;
        this.instructor = (instructor == null || instructor.isBlank()) ? "TBA" : instructor;
        this.course = (course == null)
                ? new Course("N/A", "N/A", new Department("Unknown"))
                : course;
    }
    public String getCrn() { return crn; }
    public String getSectionNumber() { return sectionNumber; }
    public String getType() { return type; }
    public Meeting getMeeting() { return meeting; }
    public String getInstructor() { return instructor; }
    public Course getCourse() { return course; }
    @Override
    public String toString() {
        String mt = (meeting == null) ? "" : " (" + meeting + ")";
        return crn + " - " + course.getCourseCode() + ": " + course.getCourseTitle() +
                " [" + type + "] - " + course.getDepartment().getDepartmentName() + mt;
    }
}
