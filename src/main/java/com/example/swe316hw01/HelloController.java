package com.example.swe316hw01;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

/**
 * GUI-only controller.
 * Handles user interactions and delegates all logic to ScheduleManager and SchedulePresenter.
 */
public class HelloController {

    private final TextField crnTextField;
    private final Canvas drawingCanvas;
    private final Button btnU, btnM, btnT, btnW, btnR;
    private final TextArea resultsTextArea;

    // Delegated helpers
    private final ScheduleManager scheduleManager;
    private final SchedulePresenter schedulePresenter;

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

        this.scheduleManager = new ScheduleManager();
        this.schedulePresenter = new SchedulePresenter(drawingCanvas);

        setupEventHandlers();
        initialize();
    }

    private void initialize() {
        String excelPath = "/Users/farahhammad/Documents/Term Schedule 251.xlsx";
        boolean loaded = scheduleManager.loadExcelData(excelPath);
        if (!loaded) {
            showError("Excel file not found or failed to load.");
            return;
        }

        schedulePresenter.loadMap("/campus_map.png", "/Users/farahhammad/Documents/campus_map.png");
    }

    private void setupEventHandlers() {
        btnU.setOnAction(e -> {
            schedulePresenter.highlightSelectedButton(btnU, btnU, btnM, btnT, btnW, btnR);
            handleDaySelection("Sunday", "U");
        });
        btnM.setOnAction(e -> {
            schedulePresenter.highlightSelectedButton(btnM, btnU, btnM, btnT, btnW, btnR);
            handleDaySelection("Monday", "M");
        });
        btnT.setOnAction(e -> {
            schedulePresenter.highlightSelectedButton(btnT, btnU, btnM, btnT, btnW, btnR);
            handleDaySelection("Tuesday", "T");
        });
        btnW.setOnAction(e -> {
            schedulePresenter.highlightSelectedButton(btnW, btnU, btnM, btnT, btnW, btnR);
            handleDaySelection("Wednesday", "W");
        });
        btnR.setOnAction(e -> {
            schedulePresenter.highlightSelectedButton(btnR, btnU, btnM, btnT, btnW, btnR);
            handleDaySelection("Thursday", "R");
        });
    }

    private void handleDaySelection(String dayName, String dayCode) {
        String input = crnTextField.getText().trim();
        if (input.isEmpty()) {
            showError("Please enter at least one CRN number.");
            return;
        }

        if (!scheduleManager.isReady()) {
            showError("Data not loaded.");
            return;
        }

        List<String> crns = Arrays.asList(input.split("\\s+"));
        ScheduleManager.ScheduleResultData data = scheduleManager.generateResult(crns, dayCode);

        if (data == null || data.schedule == null) {
            showError("No courses found for " + dayName);
            return;
        }

        // format + draw
        String summary = schedulePresenter.formatSummary(
                crns.toArray(new String[0]),
                data.schedule,
                data.numberOfBuildings,
                data.totalDistance
        );
        resultsTextArea.setText(summary);
        schedulePresenter.drawRoute(data.routeBuildings);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
