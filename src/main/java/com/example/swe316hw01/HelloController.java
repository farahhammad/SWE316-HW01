package com.example.swe316hw01;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.util.List;

public class HelloController {

    private TextField crnTextField;
    private Canvas drawingCanvas;
    private Button btnU, btnM, btnT, btnW, btnR;
    private TextArea resultsTextArea;

    private ExcelReader excelReader;
    private Image campusMapImage;
    private RouteVisualizer routeVisualizer;

    private String selectedDay = "";

    public HelloController(TextField crnTextField, Canvas drawingCanvas,
                           Button btnU, Button btnM, Button btnT, Button btnW, Button btnR,
                           TextArea resultsTextArea) {
        this.crnTextField = crnTextField;
        this.drawingCanvas = drawingCanvas;
        this.btnU = btnU;
        this.btnM = btnM;
        this.btnT = btnT;
        this.btnW = btnW;
        this.btnR = btnR;
        this.resultsTextArea = resultsTextArea;

        this.excelReader = new ExcelReader();
        this.routeVisualizer = new RouteVisualizer(drawingCanvas.getGraphicsContext2D());


        loadCampusMap();
        autoLoadExcelData();
        setupEventHandlers();
//        enableMapClickCoordinates();

        System.out.println("Controller initialized successfully!");
    }

    private void setupEventHandlers() {
        btnU.setOnAction(event -> handleDaySelection("Sunday", "U", btnU));
        btnM.setOnAction(event -> handleDaySelection("Monday", "M", btnM));
        btnT.setOnAction(event -> handleDaySelection("Tuesday", "T", btnT));
        btnW.setOnAction(event -> handleDaySelection("Wednesday", "W", btnW));
        btnR.setOnAction(event -> handleDaySelection("Thursday", "R", btnR));
    }

    private void autoLoadExcelData() {
        try {
            String filePath = "/Users/farahhammad/Documents/Term Schedule 251.xlsx";
            File file = new File(filePath);

            if (!file.exists()) {
                showError("File Not Found",
                        "Excel file not found at:\n" + filePath +
                                "\n\nPlease place the file in the correct location.");
                return;
            }

            System.out.println("Auto-loading Excel data...");
            excelReader.readExcelFile(filePath);
            System.out.println("Excel data loaded successfully!");
            System.out.println("Total sections created: " + excelReader.getAllSections().size());

        } catch (Exception ex) {
            showError("Error loading Excel file", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadCampusMap() {
        try {
            String imagePath = "/campus_map.png";
            campusMapImage = new Image(getClass().getResourceAsStream(imagePath));
            drawCampusMap();
            System.out.println("Campus map loaded successfully!");
        } catch (Exception e) {
            System.err.println("Could not load campus map from resources.");
            try {
                String imagePath = "file:///Users/farahhammad/Documents/campus_map.png";
                campusMapImage = new Image(imagePath);
                drawCampusMap();
                System.out.println("Campus map loaded from Documents folder!");
            } catch (Exception ex) {
                System.err.println("Error loading campus map: " + ex.getMessage());
            }
        }
    }

    private void drawCampusMap() {
        if (campusMapImage != null) {
            GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
            gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
            gc.drawImage(campusMapImage, 0, 0,
                    drawingCanvas.getWidth(),
                    drawingCanvas.getHeight());
        }
    }

    private void handleDaySelection(String dayName, String dayCode, Button clickedButton) {
        selectedDay = dayCode;

        resetButtonStyles();
        clickedButton.setStyle("-fx-min-width: 50px; -fx-min-height: 40px; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-background-color: #90EE90;");

        String crnInput = crnTextField.getText().trim();

        if (crnInput.isEmpty()) {
            showError("No CRNs Entered", "Please enter at least one CRN number.");
            return;
        }

        String[] crnArray = crnInput.split("\\s+");

        // Get sections by CRNs
        List<Section> selectedSections = excelReader.getSectionsByCRNs(crnArray);

        // Filter by selected day
        List<Section> sectionsForDay = excelReader.filterSectionsByDay(selectedSections, dayCode);

        // Create StudentSchedule object - it handles all calculations
        StudentSchedule schedule = new StudentSchedule(dayName, sectionsForDay);

        // Display results
        displayResults(crnArray, schedule);

        // Draw route on the map
        drawRouteOnMap(schedule);

        System.out.println("Day selected: " + dayName + " (" + dayCode + ")");
        System.out.println("CRNs entered: " + java.util.Arrays.toString(crnArray));
        System.out.println("Sections found for " + dayName + ": " + sectionsForDay.size());
    }

    private void resetButtonStyles() {
        String defaultStyle = "-fx-min-width: 50px; -fx-min-height: 40px; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-background-color: #cccccc;";
        btnU.setStyle(defaultStyle);
        btnM.setStyle(defaultStyle);
        btnT.setStyle(defaultStyle);
        btnW.setStyle(defaultStyle);
        btnR.setStyle(defaultStyle);
    }

    // Simplified displayResults - uses StudentSchedule
    private void displayResults(String[] crns, StudentSchedule schedule) {
        StringBuilder results = new StringBuilder();

        results.append("Selected Day: ").append(schedule.getSelectedDay()).append("\n");
        results.append("Number of Courses = ").append(schedule.getNumberOfCourses()).append("\n\n");

        // Display entered CRNs
        results.append("Entered CRNs: ");
        for (int i = 0; i < crns.length; i++) {
            results.append(crns[i]);
            if (i < crns.length - 1) {
                results.append(", ");
            }
        }
        results.append("\n\n");

        // Display courses
        List<Section> sections = schedule.getSections();
        if (sections.isEmpty()) {
            results.append("No courses found for the entered CRNs on ")
                    .append(schedule.getSelectedDay()).append(".\n");
        } else {
            for (int i = 0; i < sections.size(); i++) {
                Section section = sections.get(i);
                results.append((i + 1)).append("- ")
                        .append(section.getCourseTitle()).append(": ")
                        .append(section.getCourseName()).append("\n");
            }
        }

        results.append("\n");

        // Use StudentSchedule methods for calculations
        results.append("Number of Different Buildings = ")
                .append(schedule.getNumberOfBuildings()).append("\n\n");

        results.append("Distance Traveled = ")
                .append(String.format("%.0f", schedule.computeDistance())).append(" m");

        resultsTextArea.setText(results.toString());
    }

    // Draw route on campus map using RouteVisualizer
    private void drawRouteOnMap(StudentSchedule schedule) {
        // Redraw campus map first (clear previous route)
        drawCampusMap();

        // Use RouteVisualizer to draw the route
        routeVisualizer.drawRoute(schedule);

        System.out.println("Route visualization completed!");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


//    private void enableMapClickCoordinates() {
//        drawingCanvas.setOnMouseClicked(event -> {
//            double x = event.getX();
//            double y = event.getY();
//
//            System.out.println("Clicked at: X = " + x + ", Y = " + y);
//            resultsTextArea.appendText("Clicked at: X = " + x + ", Y = " + y + "\n");
//        });
//    }

}