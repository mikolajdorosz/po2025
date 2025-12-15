package org.example.simulatorgui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import simulator.*;

import java.util.Locale;

public class ClutchController {
    @FXML
    private TitledPane clutchPane;
    @FXML
    private TextField clutchNameTextField;
    @FXML
    private TextField clutchPriceTextField;
    @FXML
    private TextField clutchWeightTextField;
    @FXML
    private TextField clutchStateTextField;
    @FXML
    private Button pressClutchButton;
    @FXML
    private Button releaseClutchButton;

    @FXML
    public void onPressClutch(ActionEvent actionEvent) {
        System.out.println("Clutch pressed!");
    }
    @FXML
    public void onReleaseClutch(ActionEvent actionEvent) {
        System.out.println("Clutch released!");
    }

    public Clutch getClutchFromInput() {
        String name = clutchNameTextField.getText();
        double weight = Double.parseDouble(clutchWeightTextField.getText());
        double price = Double.parseDouble(clutchPriceTextField.getText());
        boolean state = clutchStateTextField.getText().toLowerCase().trim().equals("pressed") ? true : false;

        return new Clutch(name, weight, price, state);
    }
}
