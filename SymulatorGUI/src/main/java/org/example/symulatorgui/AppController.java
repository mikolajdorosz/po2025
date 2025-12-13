package org.example.symulatorgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AppController {
    // <!-- Samochód -->
    @FXML
    private TextField carModelTextField;
    @FXML
    private TextField carNumberTextField;
    @FXML
    private TextField carWeightTextField;
    @FXML
    private TextField carSpeedTextField;
    @FXML
    private Button startCarButton;
    @FXML
    private Button turnOffCarButton;

    @FXML
    public void onStartCar(ActionEvent actionEvent) {

    }
    @FXML
    public void onTurnOffCar(ActionEvent actionEvent) {
        System.out.println("Turning off car");
    }

    // <!-- Skrzynia Biegów -->
    @FXML
    private TextField gearboxNameTextField;
    @FXML
    private TextField gearboxPriceTextField;
    @FXML
    private TextField gearboxWeightTextField;
    @FXML
    private TextField gearTextField;
    @FXML
    private Button gearUpButton;
    @FXML
    private Button gearDownButton;

    @FXML
    public void onGearUp(ActionEvent actionEvent) {
        System.out.println("One gear up!");
    }
    @FXML
    public void onGearDown(ActionEvent actionEvent) {
        System.out.println("One gear down!");
    }

    // <!-- Silnik -->
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

    // <!-- Sprzęgło -->
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


    @FXML
    private Button addCarButton;
    @FXML
    private Button deleteCarButton;
    @FXML
    private ComboBox carComboBox;
    @FXML
    public void onAddCar(ActionEvent actionEvent) {
        System.out.println("Added car!");
    }
    @FXML
    public void onDeleteCar(ActionEvent actionEvent) {
        System.out.println("Deleted car!");
    }
}
