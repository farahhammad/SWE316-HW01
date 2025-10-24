package com.example.swe316hw01;

import java.time.LocalTime;
import java.util.*;

/**
 * Application service:
 * - Reads sections by CRNs from repository
 * - Filters by day (U/M/T/W/R)
 * - Sorts chronologically (null start times last)
 * - Returns a data-only StudentSchedule
 * - Provides all derived computations (route buildings, distance, #buildings)
 */
public class ScheduleService {

    private final SectionsHolder repo;

    public ScheduleService(SectionsHolder repo) {
        this.repo = repo;
    }

    /** Build a data-only schedule (no computations inside the object). */
    public StudentSchedule buildSchedule(Collection<String> crns, String dayCode) {
        List<Section> filtered = repo.findByCrnsAndDay(crns, dayCode);
        filtered.sort(Comparator
                .comparing(this::startTimeOf, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Section::getCrn));
        return new StudentSchedule(dayNameFromCode(dayCode), filtered);
    }

    /** Compute unique-in-order buildings from the schedule (for route + count). */
    public List<Building> computeRouteBuildings(StudentSchedule schedule) {
        List<Building> unique = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (Section s : schedule.getSections()) {
            if (s == null || s.getMeeting() == null) continue;
            Building b = s.getMeeting().getBuilding();
            if (b != null && seen.add(b.getBuildingNumber())) {
                unique.add(b);
            }
        }
        return unique;
    }

    /** Count of different buildings (uses the unique-in-order list). */
    public int computeNumberOfDifferentBuildings(StudentSchedule schedule) {
        return computeRouteBuildings(schedule).size();
    }

    /** Total distance over the unique-in-order buildings path. */
    public double computeTotalDistanceMeters(StudentSchedule schedule) {
        return DistanceCalculator.calculateBuildingRouteDistance(computeRouteBuildings(schedule));
    }

    // ---- helpers ----
    private LocalTime startTimeOf(Section s) {
        if (s == null || s.getMeeting() == null) return null;
        String raw = s.getMeeting().getStartTime();
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.isEmpty() || raw.equalsIgnoreCase("N/A")) return null;
        try {
            int v = Integer.parseInt(raw); // "800" or "1030"
            int h = v / 100;
            int m = v % 100;
            if (h < 0 || h > 23 || m < 0 || m > 59) return null;
            return java.time.LocalTime.of(h, m);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String dayNameFromCode(String c) {
        if (c == null) return "";
        return switch (c.toUpperCase()) {
            case "U" -> "Sunday";
            case "M" -> "Monday";
            case "T" -> "Tuesday";
            case "W" -> "Wednesday";
            case "R" -> "Thursday";
            default -> c;
        };
    }
}
