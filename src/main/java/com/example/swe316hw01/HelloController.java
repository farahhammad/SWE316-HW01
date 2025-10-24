package com.example.swe316hw01;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Thin UI controller:
 * - Reads inputs (CRNs, day)
 * - Delegates building and computations to ScheduleService
 * - Delegates formatting to ScheduleFormatter
 * - Delegates drawing to RouteVisualizer
 */
public class HelloController {

    // ---- UI references ----
    private final TextField crnTextField;
    private final Canvas drawingCanvas;
    private final Button btnU, btnM, btnT, btnW, btnR;
    private final TextArea resultsTextArea;

    // ---- Data loading and access ----
    private ExcelScheduleLoader excelLoader;                 // loads from Excel
    private InMemorySectionRepository sectionRepo;           // in-memory store

    // ---- Application layer ----
    private ScheduleService scheduleService;                 // builds schedule + computations
    private ScheduleFormatter scheduleFormatter;             // formats summary text

    // ---- Visualization ----
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

        // Init collaborators
        this.excelLoader = new ExcelScheduleLoader();
        this.scheduleFormatter = new ScheduleFormatter();
        this.routeVisualizer = new RouteVisualizer(drawingCanvas.getGraphicsContext2D());

        loadCampusMap();
        autoLoadExcelData();   // sets repo + service
        setupEventHandlers();

        System.out.println("Controller initialized successfully!");
    }

    // ---------------- UI event handlers ----------------

    private void setupEventHandlers() {
        btnU.setOnAction(event -> handleDaySelection("Sunday", "U", btnU));
        btnM.setOnAction(event -> handleDaySelection("Monday", "M", btnM));
        btnT.setOnAction(event -> handleDaySelection("Tuesday", "T", btnT));
        btnW.setOnAction(event -> handleDaySelection("Wednesday", "W", btnW));
        btnR.setOnAction(event -> handleDaySelection("Thursday", "R", btnR));
    }

    // ---------------- Data loading ----------------

    /**
     * Loads Excel, stores sections in memory, and initializes ScheduleService.
     */
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
            List<Section> data = excelLoader.load(filePath);
            this.sectionRepo = new InMemorySectionRepository(data);
            this.scheduleService = new ScheduleService(sectionRepo);   // computations live here
            System.out.println("Excel data loaded successfully!");
            System.out.println("Total sections created: " + data.size());

        } catch (Exception ex) {
            showError("Error loading Excel file", ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ---------------- Map loading/drawing ----------------

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

    // ---------------- Main interaction: day selection ----------------

    /**
     * Delegates to ScheduleService for building and computations,
     * ScheduleFormatter for text, and RouteVisualizer for drawing.
     */
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
        List<String> crns = Arrays.asList(crnArray);

        if (scheduleService == null) {
            showError("Data not loaded", "Excel data repository is not loaded yet.");
            return;
        }

        // Build a data-only schedule (sorted chronologically)
        StudentSchedule schedule = scheduleService.buildSchedule(crns, dayCode);

        // Compute derived values via the service
        int numBuildings = scheduleService.computeNumberOfDifferentBuildings(schedule);
        double distance  = scheduleService.computeTotalDistanceMeters(schedule);
        List<Building> routeBuildings = scheduleService.computeRouteBuildings(schedule);

        // Format summary via the formatter
        String summary = scheduleFormatter.formatSummary(crnArray, schedule, numBuildings, distance);
        resultsTextArea.setText(summary);

        // Draw route using the computed buildings
        drawRouteOnMap(routeBuildings);

        System.out.println("Day selected: " + dayName + " (" + dayCode + ")");
        System.out.println("CRNs entered: " + Arrays.toString(crnArray));
        System.out.println("Sections found: " + schedule.getNumberOfCourses());
    }

    private void drawRouteOnMap(List<Building> buildings) {
        drawCampusMap(); // clear previous
        routeVisualizer.drawRoute(buildings);
        System.out.println("Route visualization completed!");
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
