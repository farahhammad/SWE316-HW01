package com.example.swe316hw01;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class RouteVisualizer {
    private GraphicsContext gc;

    public RouteVisualizer(GraphicsContext gc) {
        this.gc = gc;
    }

    // Draw route for a student schedule
    public void drawRoute(StudentSchedule schedule) {
        List<Building> buildings = schedule.getBuildings();

        if (buildings.isEmpty()) {
            return;
        }

        // Draw lines connecting buildings
        drawConnectingLines(buildings);

        // Draw numbered markers at each building
        drawBuildingMarkers(buildings);
    }

    // Draw lines between buildings
    private void drawConnectingLines(List<Building> buildings) {
        if (buildings.size() < 2) {
            return;
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        for (int i = 0; i < buildings.size() - 1; i++) {
            Point p1 = buildings.get(i).getLocation();
            Point p2 = buildings.get(i + 1).getLocation();
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    // Draw numbered circles at building locations
    private void drawBuildingMarkers(List<Building> buildings) {
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int i = 0; i < buildings.size(); i++) {
            Point location = buildings.get(i).getLocation();
            double x = location.getX();
            double y = location.getY();

            // Draw circle
            gc.fillOval(x - 20, y - 20, 40, 40);
            gc.strokeOval(x - 20, y - 20, 40, 40);

            // Draw number inside circle
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Arial", 20));
            gc.fillText(String.valueOf(i + 1), x - 7, y + 7);
            gc.setFill(Color.YELLOW);
        }
    }
}