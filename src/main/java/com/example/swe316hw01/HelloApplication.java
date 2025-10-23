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

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        // Create the main container
        AnchorPane root = new AnchorPane();
        root.setPadding(new Insets(10));

        // (A) CRN Input Field at top
        Label crnLabel = new Label("Enter Student CRN Numbers:");
        crnLabel.setLayoutX(20);
        crnLabel.setLayoutY(15);
        crnLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextField crnTextField = new TextField();
        crnTextField.setLayoutX(220);
        crnTextField.setLayoutY(10);
        crnTextField.setPrefWidth(900);
        crnTextField.setPrefHeight(30);
        crnTextField.setPromptText("Enter CRN numbers separated by spaces (e.g., 11695 14313 10744 15375)");

        // (B) Canvas for Drawing - Visualization Area (Campus Map)
        Canvas drawingCanvas = new Canvas(700, 650);
        drawingCanvas.setLayoutX(20);
        drawingCanvas.setLayoutY(60);
        drawingCanvas.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        // (C) Day Selection Buttons - Results Label
        Label resultsLabel = new Label("Results");
        resultsLabel.setLayoutX(750);
        resultsLabel.setLayoutY(60);
        resultsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Day Buttons Container
        HBox dayButtonsBox = new HBox(10);
        dayButtonsBox.setLayoutX(750);
        dayButtonsBox.setLayoutY(90);

        Button btnU = new Button("U");
        Button btnM = new Button("M");
        Button btnT = new Button("T");
        Button btnW = new Button("W");
        Button btnR = new Button("R");

        // Style for day buttons
        String buttonStyle = "-fx-min-width: 50px; -fx-min-height: 40px; -fx-font-size: 14px; -fx-font-weight: bold;";
        btnU.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnM.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnT.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnW.setStyle(buttonStyle + "-fx-background-color: #cccccc;");
        btnR.setStyle(buttonStyle + "-fx-background-color: #cccccc;");

        dayButtonsBox.getChildren().addAll(btnU, btnM, btnT, btnW, btnR);

        // (D) Results Display Area
        TextArea resultsTextArea = new TextArea();
        resultsTextArea.setLayoutX(750);
        resultsTextArea.setLayoutY(150);
        resultsTextArea.setPrefWidth(420);
        resultsTextArea.setPrefHeight(560);
        resultsTextArea.setEditable(false);
        resultsTextArea.setWrapText(true);
        resultsTextArea.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New';");

        // Create the controller and pass the components
        HelloController controller = new HelloController(
                crnTextField, drawingCanvas,
                btnU, btnM, btnT, btnW, btnR,
                resultsTextArea
        );

        // Add all components to the root
        root.getChildren().addAll(
                crnLabel, crnTextField,
                drawingCanvas,
                resultsLabel, dayButtonsBox,
                resultsTextArea
        );

        // Create scene and show stage
        Scene scene = new Scene(root, 1200, 750);
        stage.setTitle("Term 251 Schedule Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}