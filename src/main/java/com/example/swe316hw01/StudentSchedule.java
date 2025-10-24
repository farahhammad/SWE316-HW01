package com.example.swe316hw01;

import java.util.*;

/**
 * Data object for a student's schedule for a specific day.
 * - Holds the selected day and chronologically sorted sections.
 * - Also carries precomputed presentation data (set by ScheduleManager).
 *   numberOfBuildings, totalDistance, routeBuildings
 * No GUI code here.
 */
public class StudentSchedule {

    private final String selectedDay;               // e.g., "Monday"
    private final List<Section> sectionsSorted;     // chronological, unmodifiable

    // Computed fields (filled by ScheduleManager after building)
    private int numberOfBuildings;
    private double totalDistance;
    private List<Building> routeBuildings = List.of(); // visiting order

    public StudentSchedule(String selectedDay, List<Section> sectionsSorted) {
        this.selectedDay = selectedDay;
        this.sectionsSorted = List.copyOf(sectionsSorted == null ? List.of() : sectionsSorted);
    }

    // ---- core getters ----
    public String getSelectedDay() { return selectedDay; }
    public List<Section> getSections() { return sectionsSorted; }
    public int getNumberOfCourses() { return sectionsSorted.size(); }

    // ---- computed data (getters + package-private setters) ----
    public int getNumberOfBuildings() { return numberOfBuildings; }
    public double getTotalDistance() { return totalDistance; }
    public List<Building> getRouteBuildings() { return routeBuildings; }

    // Set by ScheduleManager (keep package-private to avoid misuse from UI)
    void setNumberOfBuildings(int n) { this.numberOfBuildings = n; }
    void setTotalDistance(double d) { this.totalDistance = d; }
    void setRouteBuildings(List<Building> list) {
        this.routeBuildings = List.copyOf(list == null ? List.of() : list);
    }
}
