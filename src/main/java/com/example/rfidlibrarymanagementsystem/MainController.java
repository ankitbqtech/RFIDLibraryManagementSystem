package com.example.rfidlibrarymanagementsystem;

import com.clou.uhf.G3Lib.Enumeration.eReadType;
import com.clou.uhf.G3Lib.Protocol.Device_Mode;
import com.clou.uhf.G3Lib.Protocol.Tag_Model;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.clou.uhf.G3Lib.CLReader;


public class MainController {

    @FXML
    private Button connectButton;

    @FXML
    private TextArea outputTextArea;
    @FXML
    private String previousEPC = null;

    private Main1 rfidReader;

    @FXML
    private void initialize() {
        rfidReader = new Main1();
    }

    @FXML
    private void ReaderConnection() {
        String ConnID = "192.168.1.116:9090";

        try {
            if (CLReader.CreateTcpConn("192.168.1.116:9090", rfidReader)) {
//                appendToOutput("Connect success!\n");

                int a = CLReader._Tag6C.GetEPC("192.168.1.116:9090", 1, eReadType.Single);

                if (a == 0) {
//                    appendToOutput("Success!" + a + "\n");
                    System.out.println("Success!" + a + "\n");

                    // Start a separate thread for the output loop
                    new Thread(() -> {
                        showEPC();
                    }).start();
                } else {
                    appendToOutput("Failure!" + a + "\n");
                }
            } else {
                appendToOutput("Connect failure!\n");
            }
            appendToOutput("Current ConnID : " + ConnID + "\n");

        } catch (Exception ex) {
            appendToOutput(ex.getMessage() + "\n");
        }
    }



    private void showEPC() {
        try {
            while (true) {
                String epcValue = rfidReader.getCurrentEPC();

                if (epcValue != null && !epcValue.equals(previousEPC)) {
                    appendToOutput("EPC: " + epcValue + "\n");
                    previousEPC = epcValue;
                }

                Thread.sleep(100); // Adjust the sleep duration
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void appendToOutput(String message) {
//        System.out.println(message); // Print to terminal
        outputTextArea.appendText(message); // Display in the application
    }
    @FXML
    private void handleTab2Action() {
        // Code for Tab 2...
    }
}
