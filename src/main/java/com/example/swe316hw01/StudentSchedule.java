package com.example.swe316hw01;

import java.util.*;

/**
 * Data-only domain object: a student's schedule for a specific day.
 * - Holds selected day name and the chronologically sorted sections.
 * - No derived computations (route buildings, distance, counts).
 * Those are done by ScheduleService.
 */
public class StudentSchedule {

    private final String selectedDay;           // e.g., "Sunday"
    private final List<Section> sectionsSorted; // chronological, unmodifiable

    public StudentSchedule(String selectedDay, List<Section> sectionsSorted) {
        this.selectedDay = selectedDay;
        this.sectionsSorted = List.copyOf(sectionsSorted == null ? List.of() : sectionsSorted);
    }

    /** Friendly day name used in UI. */
    public String getSelectedDay() { return selectedDay; }

    /** Chronological sections list (already sorted). */
    public List<Section> getSections() { return sectionsSorted; }

    /** Convenience for UI—count without computing anything new. */
    public int getNumberOfCourses() { return sectionsSorted.size(); }
}
