package com.example.swe316hw01;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a student's schedule for a specific day
 * Single Responsibility: Manages schedule calculations and data
 */
public class StudentSchedule {
    private String selectedDay;
    private List<Section> sections;

    public StudentSchedule(String selectedDay, List<Section> sections) {
        this.selectedDay = selectedDay;
        this.sections = sections;
    }

    public String getSelectedDay() {
        return selectedDay;
    }

    public int getNumberOfCourses() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    // Get list of unique buildings in order
    public List<Building> getBuildings() {
        List<Building> buildings = new ArrayList<>();
        Set<String> seenBuildings = new HashSet<>();

        for (Section section : sections) {
            Building building = section.getMeeting().getBuilding();
            if (building != null && !seenBuildings.contains(building.getBuildingNumber())) {
                buildings.add(building);
                seenBuildings.add(building.getBuildingNumber());
            }
        }

        return buildings;
    }

    // Calculate number of unique buildings
    public int getNumberOfBuildings() {
        return getBuildings().size();
    }

    // Calculate total distance traveled - delegates to DistanceCalculator
    public double computeDistance() {
        List<Building> buildings = getBuildings();
        return DistanceCalculator.calculateBuildingRouteDistance(buildings);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Selected Day: ").append(selectedDay).append("\n");
        sb.append("Number of Courses = ").append(getNumberOfCourses()).append("\n\n");

        for (int i = 0; i < sections.size(); i++) {
            sb.append((i + 1)).append("- ")
                    .append(sections.get(i).getCourseTitle()).append(": ")
                    .append(sections.get(i).getCourseName()).append("\n");
        }

        sb.append("\nNumber of Different Buildings = ").append(getNumberOfBuildings()).append("\n\n");
        sb.append("Distance Traveled = ").append(String.format("%.0f", computeDistance())).append(" m");

        return sb.toString();
    }
}