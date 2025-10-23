package com.example.swe316hw01;

/**
 * Represents a meeting time for a section
 * Single Responsibility: Manages meeting schedule details
 */
public class Meeting {
    private String days;       // e.g., "MW", "UTR"
    private String startTime;  // e.g., "0800"
    private String endTime;    // e.g., "0915"
    private Room room;
    private Building building; // Direct reference to building

    public Meeting(String days, String startTime, String endTime, Room room, Building building) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.building = building;
    }

    public String getDays() {
        return days;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Room getRoom() {
        return room;
    }

    public Building getBuilding() {
        return building;
    }

    // Check if meeting is on a specific day
    public boolean isOnDay(String day) {
        return days.contains(day);
    }

    @Override
    public String toString() {
        return days + " " + startTime + "-" + endTime + " in " + room + ", Building " + building.getBuildingNumber();
    }
}