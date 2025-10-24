package com.example.swe316hw01;

import java.time.LocalTime;

/**
 * Represents a meeting time for a section
 * Single Responsibility: Manages meeting schedule details
 */
public class Meeting {
    private String days;       // e.g., "MW", "UTR"
    private String startTime;  // e.g., "0800" or "800" or "N/A"
    private String endTime;    // e.g., "0915" or "N/A"
    private Room room;
    private Building building; // Direct reference to building

    public Meeting(String days, String startTime, String endTime, Room room, Building building) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.building = building;
    }

    public String getDays() { return days; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public Room getRoom() { return room; }
    public Building getBuilding() { return building; }

    // ---- Replace this method in Meeting.java ----
    public boolean isOnDay(String dayCode) {
        if (dayCode == null) return false;
        if (days == null) return false;

        // Normalize both sides: remove spaces and uppercase
        String normDays = days.replaceAll("\\s+", "").toUpperCase();   // e.g., " UTR " -> "UTR"
        String normCode = dayCode.trim().toUpperCase();                // e.g., "u" -> "U"

        if (normDays.equals("N/A") || normDays.isEmpty()) return false;

        // Now simple contains works
        return normDays.indexOf(normCode) >= 0;
    }


    // ---- New: parsing helpers for time ----
    public LocalTime getStartAsTime() { return parseHhmmToLocalTime(startTime); }
    public LocalTime getEndAsTime()   { return parseHhmmToLocalTime(endTime); }

    private static LocalTime parseHhmmToLocalTime(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty() || s.equalsIgnoreCase("N/A")) return null;
        // ExcelReader يحول الأرقام إلى int (800 بدل 0800) — هذا مناسب هنا
        try {
            int v = Integer.parseInt(s);
            int h = v / 100;
            int m = v % 100;
            if (h < 0 || h > 23 || m < 0 || m > 59) return null;
            return LocalTime.of(h, m);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return days + " " + startTime + "-" + endTime + " in " + room + ", Building " + building.getBuildingNumber();
    }
}
