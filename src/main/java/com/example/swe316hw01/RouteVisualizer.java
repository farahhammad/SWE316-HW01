package com.example.swe316hw01;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Render the route and markers on the canvas.
 * Input is an ordered list of buildings (presentation data), not a domain object.
 */
public class RouteVisualizer {

    private final GraphicsContext gc;

    public RouteVisualizer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draws the route given an ordered list of buildings.
     * The list should already be in visiting order.
     */
    public void drawRoute(List<Building> buildings) {
        if (buildings == null || buildings.isEmpty()) return;

        drawConnectingLines(buildings);
        drawBuildingMarkers(buildings);
    }

    // Draw lines between consecutive buildings
    private void drawConnectingLines(List<Building> buildings) {
        if (buildings.size() < 2) return;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

        for (int i = 0; i < buildings.size() - 1; i++) {
            Point p1 = buildings.get(i).getLocation();
            Point p2 = buildings.get(i + 1).getLocation();
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    // Draw numbered circles at each building location
    private void drawBuildingMarkers(List<Building> buildings) {
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        for (int i = 0; i < buildings.size(); i++) {
            Point location = buildings.get(i).getLocation();
            double x = location.getX();
            double y = location.getY();

            // Circle
            gc.fillOval(x - 20, y - 20, 40, 40);
            gc.strokeOval(x - 20, y - 20, 40, 40);

            // Number
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Arial", 20));
            gc.fillText(String.valueOf(i + 1), x - 7, y + 7);

            // Reset fill for next marker
            gc.setFill(Color.YELLOW);
        }
    }
}
