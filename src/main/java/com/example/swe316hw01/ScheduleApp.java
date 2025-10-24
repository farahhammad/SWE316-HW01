package com.example.swe316hw01;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class ScheduleApp extends Application {

    // --- UI nodes ---
    private TextField crnTextField;
    private Canvas drawingCanvas;
    private Button btnU, btnM, btnT, btnW, btnR;
    private TextArea resultsTextArea;

    // --- Non-UI helpers ---
    private ScheduleManager scheduleManager;
    private SchedulePresenter schedulePresenter;

    @Override
    public void start(Stage stage) {
        // root
        AnchorPane root = new AnchorPane();
        root.setPadding(new Insets(10));

        // Top: CRN input
        Label crnLabel = new Label("Enter Student CRN Numbers:");
        crnLabel.setLayoutX(20);
        crnLabel.setLayoutY(15);
        crnLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        crnTextField = new TextField();
        crnTextField.setLayoutX(220);
        crnTextField.setLayoutY(10);
        crnTextField.setPrefWidth(900);
        crnTextField.setPrefHeight(30);
        crnTextField.setPromptText("Enter CRNs separated by spaces (e.g., 11695 14313 10744)");

        // Map canvas
        drawingCanvas = new Canvas(700, 650);
        drawingCanvas.setLayoutX(20);
        drawingCanvas.setLayoutY(60);
        drawingCanvas.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        // Day buttons
        Label resultsLabel = new Label("Results");
        resultsLabel.setLayoutX(750);
        resultsLabel.setLayoutY(60);
        resultsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox dayButtonsBox = new HBox(10);
        dayButtonsBox.setLayoutX(750);
        dayButtonsBox.setLayoutY(90);

        btnU = new Button("U");
        btnM = new Button("M");
        btnT = new Button("T");
        btnW = new Button("W");
        btnR = new Button("R");

        String buttonStyle = "-fx-min-width: 50px; -fx-min-height: 40px; -fx-font-size: 14px; -fx-font-weight: bold;";
        btnU.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnM.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnT.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnW.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnR.setStyle(buttonStyle + "-fx-background-color: #cccccc;");

        dayButtonsBox.getChildren().addAll(btnU, btnM, btnT, btnW, btnR);

        // Results area
        resultsTextArea = new TextArea();
        resultsTextArea.setLayoutX(750);
        resultsTextArea.setLayoutY(150);
        resultsTextArea.setPrefWidth(420);
        resultsTextArea.setPrefHeight(560);
        resultsTextArea.setEditable(false);
        resultsTextArea.setWrapText(true);
        resultsTextArea.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New';");

        // Add to root
        root.getChildren().addAll(crnLabel, crnTextField, drawingCanvas, resultsLabel, dayButtonsBox, resultsTextArea);

        // Scene
        Scene scene = new Scene(root, 1200, 750);
        stage.setTitle("Term 251 Schedule Visualizer");
        stage.setScene(scene);
        stage.show();

        // Initialize collaborators and wire events
        scheduleManager = new ScheduleManager();
        schedulePresenter = new SchedulePresenter(drawingCanvas);

        initializeDataAndMap();
        setupDayHandlers();
    }

    private void initializeDataAndMap() {
        String excelPath = "/Users/farahhammad/Documents/Term Schedule 251.xlsx";
        boolean ok = scheduleManager.loadExcelData(excelPath);
        if (!ok) {
            showError("Excel file not found or failed to load:\n" + excelPath);
        }
        schedulePresenter.loadMap("/campus_map.png", "/Users/farahhammad/Documents/campus_map.png");
    }

    private void setupDayHandlers() {
        btnU.setOnAction(e -> { highlight(btnU); handleDay("Sunday","U"); });
        btnM.setOnAction(e -> { highlight(btnM); handleDay("Monday","M"); });
        btnT.setOnAction(e -> { highlight(btnT); handleDay("Tuesday","T"); });
        btnW.setOnAction(e -> { highlight(btnW); handleDay("Wednesday","W"); });
        btnR.setOnAction(e -> { highlight(btnR); handleDay("Thursday","R"); });
    }

    // button highlight
    private void highlight(Button selected) {
        schedulePresenter.highlightSelectedButton(selected, btnU, btnM, btnT, btnW, btnR);
    }

    private void handleDay(String dayName, String dayCode) {
        String input = crnTextField.getText() == null ? "" : crnTextField.getText().trim();
        if (input.isEmpty()) {
            showError("Please enter at least one CRN number.");
            return;
        }
        if (!scheduleManager.isReady()) {
            showError("Data not loaded.");
            return;
        }

        List<String> crns = Arrays.asList(input.split("\\s+"));
        StudentSchedule schedule = scheduleManager.generateResult(crns, dayCode);
        if (schedule == null || schedule.getNumberOfCourses() == 0) {
            showError("No courses found for " + dayName + ".");
            return;
        }

        // format & draw using the schedule’s computed fields
        String summary = schedulePresenter.formatSummary(
                crns.toArray(new String[0]),
                schedule,
                schedule.getNumberOfBuildings(),
                schedule.getTotalDistance()
        );
        resultsTextArea.setText(summary);

        schedulePresenter.drawRoute(schedule.getRouteBuildings());
    }

    private void showError(String msg) {
        var a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
