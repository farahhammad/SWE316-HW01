package com.example.swe316hw01;

/**
 * Represents a meeting time for a section
 * Manages meeting schedule details
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


    public boolean isOnDay(String dayCode) {
        if (dayCode == null) return false;
        if (days == null) return false;

        // Normalize both sides: remove spaces and uppercase
        String normDays = days.replaceAll("\\s+", "").toUpperCase();
        String normCode = dayCode.trim().toUpperCase();                // e.g., "u" -> "U"

        if (normDays.equals("N/A") || normDays.isEmpty()) return false;
        return normDays.indexOf(normCode) >= 0;
    }

    @Override
    public String toString() {
        return days + " " + startTime + "-" + endTime + " in " + room + ", Building " + building.getBuildingNumber();
    }
}
