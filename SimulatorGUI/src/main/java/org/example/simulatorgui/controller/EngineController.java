package org.example.simulatorgui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import simulator.*;

public class EngineController {
    @FXML
    private TitledPane enginePane;
    @FXML
    private TextField engineNameTextField;
    @FXML
    private TextField enginePriceTextField;
    @FXML
    private TextField engineWeightTextField;
    @FXML
    private TextField engineSpeedTextField;
    @FXML
    private Button speedUpButton;
    @FXML
    private Button slowDownButton;

    @FXML
    public void onSpeedUp(ActionEvent actionEvent) {
        System.out.println("Car is accelerating!");
    }
    @FXML
    public void onSlowDown(ActionEvent actionEvent) {
        System.out.println("Car is slowing down!");
    }

    public Engine getEngineFromInput() {
        String name = engineNameTextField.getText();
        double weight = Double.parseDouble(engineWeightTextField.getText());
        double price = Double.parseDouble(enginePriceTextField.getText());
        int maxRPM = Integer.parseInt(enginePriceTextField.getText());

        return new Engine(maxRPM, name, weight, price);
    }
}
