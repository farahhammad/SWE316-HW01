package com.example.swe316hw01;

import java.util.List;

/**
 * Presentation helper: produce the right-panel summary text.
 * It receives precomputed numbers from ScheduleService.
 */
public class ScheduleFormatter {

    public String formatSummary(String[] crns,
                                StudentSchedule schedule,
                                int numberOfDifferentBuildings,
                                double totalDistanceMeters) {

        StringBuilder sb = new StringBuilder();

        sb.append("Selected Day: ").append(schedule.getSelectedDay()).append("\n");
        sb.append("Number of Courses = ").append(schedule.getNumberOfCourses()).append("\n\n");

        // Echo CRNs
        sb.append("Entered CRNs: ");
        for (int i = 0; i < crns.length; i++) {
            sb.append(crns[i]);
            if (i < crns.length - 1) sb.append(", ");
        }
        sb.append("\n\n");

        // List courses (already chronological)
        List<Section> sections = schedule.getSections();
        if (sections.isEmpty()) {
            sb.append("No courses found for the entered CRNs on ")
                    .append(schedule.getSelectedDay()).append(".\n");
        } else {
            for (int i = 0; i < sections.size(); i++) {
                Section section = sections.get(i);
                sb.append(i + 1).append("- ")
                        .append(section.getCourseTitle()).append(": ")
                        .append(section.getCourseName());

                String t = (section.getMeeting() != null) ? section.getMeeting().getStartTime() : null;
                if (t != null && !t.isBlank() && !"N/A".equalsIgnoreCase(t)) {
                    sb.append(" (").append(t).append(")");
                }
                sb.append("\n");
            }
        }

        sb.append("\n");
        sb.append("Number of Different Buildings = ")
                .append(numberOfDifferentBuildings).append("\n\n");

        sb.append("Distance Traveled = ")
                .append(String.format("%.0f", totalDistanceMeters)).append(" m");

        return sb.toString();
    }
}
