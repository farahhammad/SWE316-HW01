package com.example.swe316hw01;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.control.Button;   // ✅ use JavaFX Button (not AWT)
import java.util.List;

/**
 * Handle presentation — map rendering + summary text formatting.
 */
public class SchedulePresenter {

    private final Canvas canvas;
    private final RouteVisualizer routeVisualizer;
    private Image campusMap;

    public SchedulePresenter(Canvas canvas) {
        this.canvas = canvas;
        this.routeVisualizer = new RouteVisualizer(canvas.getGraphicsContext2D());
    }

    /** Load or reload the campus map. */
    public void loadMap(String resourcePath, String fallbackFile) {
        try {
            campusMap = new Image(getClass().getResourceAsStream(resourcePath));
        } catch (Exception e) {
            System.err.println("Resource map not found; trying fallback path...");
            try {
                campusMap = new Image("file:///" + fallbackFile);
            } catch (Exception ex) {
                System.err.println("Error loading map: " + ex.getMessage());
            }
        }
        redrawMap();
    }

    /** Draws the map image. */
    public void redrawMap() {
        if (campusMap == null) return;
        var gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(campusMap, 0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /** Draws the full route. */
    public void drawRoute(List<Building> buildings) {
        redrawMap();
        routeVisualizer.drawRoute(buildings);
    }

    /** Returns a formatted schedule summary string. */
    public String formatSummary(String[] crns, StudentSchedule schedule, int numBuildings, double distance) {
        StringBuilder sb = new StringBuilder();

        sb.append("Entered CRNs: ");
        for (int i = 0; i < crns.length; i++) {
            sb.append(crns[i]);
            if (i < crns.length - 1) sb.append(", ");
        }
        sb.append("\n\n");

        sb.append("Selected Day: ").append(schedule.getSelectedDay()).append("\n");
        sb.append("Number of Courses = ").append(schedule.getNumberOfCourses()).append("\n\n");


        var sections = schedule.getSections();
        if (sections.isEmpty()) {
            sb.append("No courses found for this day.\n");
        } else {
            for (int i = 0; i < sections.size(); i++) {
                var s = sections.get(i);
                sb.append(i + 1).append("- ").append(s.getCourseTitle()).append(": ").append(s.getCourseName());
                var t = (s.getMeeting() != null) ? s.getMeeting().getStartTime() : "";
                if (t != null && !t.isBlank() && !"N/A".equalsIgnoreCase(t)) {
                    sb.append(" (").append(t).append(")");
                }
                sb.append("\n");
            }
        }

        sb.append("\nNumber of Different Buildings = ").append(numBuildings).append("\n\n");
        sb.append("Distance Traveled = ").append(String.format("%.0f", distance)).append(" m");

        return sb.toString();
    }


    // Day-button highlighting (GUI-level, JavaFX buttons)
    public void highlightSelectedButton(Button selected, Button... allButtons) {
        String defaultStyle = "-fx-min-width: 50px; -fx-min-height: 40px; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; "
                + "-fx-background-color: #cccccc;";
        String selectedStyle = "-fx-min-width: 50px; -fx-min-height: 40px; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; "
                + "-fx-background-color: #90EE90;";

        for (Button b : allButtons) {
            if (b == null) continue;
            b.setStyle(b == selected ? selectedStyle : defaultStyle);
        }
    }
}
