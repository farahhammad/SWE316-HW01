package com.example.swe316hw01;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a student's schedule for a specific day
 * Single Responsibility: Manages schedule calculations and data
 */
public class StudentSchedule {
    private String selectedDay;
    private List<Section> sections; // will be kept sorted by start time (nulls last)

    // Comparator: sections with time first (earlier first), then tie-breakers
    private static final Comparator<Section> BY_START_TIME = Comparator
            .comparing((Section s) -> {
                Meeting m = s.getMeeting();
                return m != null ? m.getStartAsTime() : null;
            }, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Section::getCrn); // ثابت لكسر التعادل

    public StudentSchedule(String selectedDay, List<Section> sections) {
        this.selectedDay = selectedDay;
        // copy & sort once here so كل شيء لاحقًا (المباني/المسافة/الرسم) يعتمد نفس الترتيب
        List<Section> copy = new ArrayList<>(sections == null ? List.of() : sections);
        copy.sort(BY_START_TIME);
        this.sections = Collections.unmodifiableList(copy);
    }

    public String getSelectedDay() { return selectedDay; }
    public int getNumberOfCourses() { return sections.size(); }
    public List<Section> getSections() { return sections; }

    // Get list of unique buildings following the sorted order
    public List<Building> getBuildings() {
        List<Building> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (Section s : sections) {
            if (s == null || s.getMeeting() == null) continue;
            Building b = s.getMeeting().getBuilding();
            if (b != null && seen.add(b.getBuildingNumber())) {
                result.add(b);
            }
        }
        return result;
    }

    public int getNumberOfBuildings() { return getBuildings().size(); }

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
            Section section = sections.get(i);
            sb.append(i + 1).append("- ")
                    .append(section.getCourseTitle()).append(": ")
                    .append(section.getCourseName());
            LocalTime t = section.getMeeting() != null ? section.getMeeting().getStartAsTime() : null;
            if (t != null) sb.append(" (").append(t).append(")");
            sb.append("\n");
        }
        sb.append("\nNumber of Different Buildings = ").append(getNumberOfBuildings()).append("\n\n");
        sb.append("Distance Traveled = ").append(String.format("%.0f", computeDistance())).append(" m");
        return sb.toString();
    }
}
