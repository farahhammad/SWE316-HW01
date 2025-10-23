package com.example.swe316hw01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a building on campus
 * Single Responsibility: Manages building information and location
 */
public class Building {
    private String buildingNumber;
    private Point location;
    private List<Room> rooms;

    // Static map of building coordinates
    private static Map<String, Point> buildingCoordinates;

    static {
        buildingCoordinates = new HashMap<>();

        // Add all building coordinates here
        buildingCoordinates.put("24", new Point(368, 548));
        buildingCoordinates.put("11", new Point(326, 539));
        buildingCoordinates.put("22", new Point(355, 476));
        buildingCoordinates.put("59", new Point(306, 298));
        buildingCoordinates.put("14", new Point(224, 422));
        buildingCoordinates.put("6", new Point(252, 304));
        buildingCoordinates.put("7", new Point(253, 355));
        buildingCoordinates.put("4", new Point(206, 226));
        buildingCoordinates.put("5", new Point(227, 254));
        buildingCoordinates.put("9", new Point(293, 417));
        buildingCoordinates.put("3", new Point(171, 212));
    }

    // Constructor - automatically gets coordinates from map
    public Building(String buildingNumber) {
        this.buildingNumber = buildingNumber;
        this.location = buildingCoordinates.getOrDefault(buildingNumber, new Point(0, 0));
        this.rooms = new ArrayList<>();
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public Point getLocation() {
        return location;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public String toString() {
        return "Building " + buildingNumber;
    }
}