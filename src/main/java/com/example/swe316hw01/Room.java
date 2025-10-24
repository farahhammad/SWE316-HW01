package com.example.swe316hw01;

public class Room {
    private final String roomNumber;
    private final Building building;

    public Room(String roomNumber, Building building) {
        this.roomNumber = (roomNumber == null || roomNumber.isBlank()) ? "TBA" : roomNumber.trim();
        this.building = (building == null) ? new Building("N/A") : building;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public String toString() {
        return building.getBuildingNumber() + "-" + roomNumber;
    }
}
