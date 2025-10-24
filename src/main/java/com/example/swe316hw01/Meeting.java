package com.example.swe316hw01;

/**
 * Represents a meeting time for a section.
 * Holds only Room; Building can be obtained from room.getBuilding().
 */
public class Meeting {
    private final String days;       // e.g., "MW", "UTR", "N/A"
    private final String startTime;  // e.g., "0800" or "800" or "N/A"
    private final String endTime;    // e.g., "0915" or "N/A"
    private final Room room;         // may be null (TBA)

    public Meeting(String days, String startTime, String endTime, Room room) {
        this.days = (days == null || days.isBlank()) ? "N/A" : days.trim();
        this.startTime = (startTime == null || startTime.isBlank()) ? "N/A" : startTime.trim();
        this.endTime = (endTime == null || endTime.isBlank()) ? "N/A" : endTime.trim();
        this.room = room; // may be null
    }

    public String getDays() { return days; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public Room getRoom() { return room; }

    public boolean isOnDay(String dayCode) {
        if (dayCode == null) return false;
        String normDays = days.replaceAll("\\s+", "").toUpperCase();
        String normCode = dayCode.trim().toUpperCase();
        if (normDays.equals("N/A") || normDays.isEmpty()) return false;
        return normDays.indexOf(normCode) >= 0;
    }

    @Override
    public String toString() {
        String roomStr = (room == null) ? "TBA" : room.toString();
        String bStr = (room != null && room.getBuilding() != null)
                ? room.getBuilding().getBuildingNumber() : "TBA";
        return days + " " + startTime + "-" + endTime + " in " + roomStr + ", Building " + bStr;
    }
}
