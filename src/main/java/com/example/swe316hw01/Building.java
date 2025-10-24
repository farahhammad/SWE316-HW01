package com.example.swe316hw01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a building on campus
 * Manages building information and location
 */
public class Building {
    private String buildingNumber;
    private Point location;
    private List<Room> rooms;
    private static Map<String, Point> buildingCoordinates;

    static {
        buildingCoordinates = new HashMap<>();
        buildingCoordinates.put("24", new Point(501, 581));
        buildingCoordinates.put("22", new Point(473, 490));
        buildingCoordinates.put("59", new Point(397, 279));
        buildingCoordinates.put("14", new Point(273, 427));
        buildingCoordinates.put("6", new Point(319, 298));
        buildingCoordinates.put("7", new Point(316, 343));
        buildingCoordinates.put("4", new Point(245, 194));
        buildingCoordinates.put("5", new Point(274, 232));
        buildingCoordinates.put("9", new Point(378, 421));
        buildingCoordinates.put("63", new Point(185, 69));
        buildingCoordinates.put("75", new Point(164, 136));
        buildingCoordinates.put("76", new Point(599, 516));
        buildingCoordinates.put("78", new Point(619, 474));
        buildingCoordinates.put("19", new Point(314, 509));
        buildingCoordinates.put("2", new Point(173, 157));
        buildingCoordinates.put("3", new Point(192, 178));
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