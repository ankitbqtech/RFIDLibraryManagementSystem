package com.example.rfidlibrarymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NewWindowController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}