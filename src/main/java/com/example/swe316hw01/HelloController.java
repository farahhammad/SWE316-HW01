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

public class HelloController {

    // --- UI references (unchanged) ---
    private TextField crnTextField;
    private Canvas drawingCanvas;
    private Button btnU, btnM, btnT, btnW, btnR;
    private TextArea resultsTextArea;

    // --- Replaced ExcelReader with Loader + Repository ---
    private ExcelScheduleLoader excelLoader;                 // NEW: stateless loader
    private InMemorySectionRepository sectionRepo;           // NEW: in-memory storage

    // --- Map/visualization (unchanged) ---
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

        // NEW: initialize loader and visualizer
        this.excelLoader = new ExcelScheduleLoader();
        this.routeVisualizer = new RouteVisualizer(drawingCanvas.getGraphicsContext2D());

        loadCampusMap();
        autoLoadExcelData();     // now uses loader + repository
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

    // ---------------- Data loading (modified) ----------------

    /**
     * Loads the Excel file, converts it to List<Section>, and keeps it in memory
     * via InMemorySectionRepository.
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
            List<Section> data = excelLoader.load(filePath);            // NEW
            this.sectionRepo = new InMemorySectionRepository(data);     // NEW
            System.out.println("Excel data loaded successfully!");
            System.out.println("Total sections created: " + data.size());

        } catch (Exception ex) {
            showError("Error loading Excel file", ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ---------------- Map loading/drawing (unchanged) ----------------

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

    // ---------------- Main interaction: day selection (modified) ----------------

    /**
     * Reads CRNs from the text field, queries repository for that day,
     * builds StudentSchedule, prints summary, and draws the route.
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

        if (sectionRepo == null) {
            showError("Data not loaded", "Excel data repository is not loaded yet.");
            return;
        }

        // NEW: query repository for CRNs + filter by day here
        List<Section> sectionsForDay = sectionRepo.findByCrnsAndDay(crns, dayCode);

        // Build schedule (schedules sort chronologically inside StudentSchedule)
        StudentSchedule schedule = new StudentSchedule(dayName, sectionsForDay);

        // Display summary and draw route
        displayResults(crnArray, schedule);
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

    // ---------------- Summary + drawing (unchanged) ----------------

    private void displayResults(String[] crns, StudentSchedule schedule) {
        StringBuilder results = new StringBuilder();

        results.append("Selected Day: ").append(schedule.getSelectedDay()).append("\n");
        results.append("Number of Courses = ").append(schedule.getNumberOfCourses()).append("\n\n");

        results.append("Entered CRNs: ");
        for (int i = 0; i < crns.length; i++) {
            results.append(crns[i]);
            if (i < crns.length - 1) results.append(", ");
        }
        results.append("\n\n");

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
        results.append("Number of Different Buildings = ")
                .append(schedule.getNumberOfBuildings()).append("\n\n");

        results.append("Distance Traveled = ")
                .append(String.format("%.0f", schedule.computeDistance())).append(" m");

        resultsTextArea.setText(results.toString());
    }

    private void drawRouteOnMap(StudentSchedule schedule) {
        drawCampusMap();                // clear previous route
        routeVisualizer.drawRoute(schedule);
        System.out.println("Route visualization completed!");
    }

    // ---------------- Helpers ----------------

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
