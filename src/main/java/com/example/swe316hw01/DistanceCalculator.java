package com.example.swe316hw01;

import java.util.List;

public class DistanceCalculator {

    // Calculate Euclidean distance between two points
    public static double calculateDistance(Point p1, Point p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Calculate total distance between buildings
    public static double calculateBuildingRouteDistance(List<Building> buildings) {
        if (buildings.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < buildings.size() - 1; i++) {
            Point p1 = buildings.get(i).getLocation();
            Point p2 = buildings.get(i + 1).getLocation();
            totalDistance += calculateDistance(p1, p2);
        }

        return totalDistance;
    }
}