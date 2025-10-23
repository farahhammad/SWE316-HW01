package com.example.swe316hw01;

public class Room {
    private String roomNumber;
    private Building building;

    public Room(String roomNumber, Building building) {
        this.roomNumber = roomNumber;
        this.building = building;
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
