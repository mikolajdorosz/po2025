package org.example.simulatorgui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import simulator.*;

public class GearboxController {
    @FXML
    private TitledPane gearboxPane;
    @FXML
    private TextField gearboxNameTextField;
    @FXML
    private TextField gearboxPriceTextField;
    @FXML
    private TextField gearboxWeightTextField;
    @FXML
    private TextField gearsTextField;
    @FXML
    private Button gearUpButton;
    @FXML
    private Button gearDownButton;

    private Gearbox activeGearbox;
    public void setActiveGearbox(Gearbox gearbox) {
        this.activeGearbox = gearbox;
        System.out.println("Active gearbox set to: " + gearbox);
    }
    public Gearbox getActiveGearbox() {
        return activeGearbox;
    }

    @FXML
    public void onGearUp() {
        if (activeGearbox != null) activeGearbox.gearUp();
        System.out.println("One gear up!");
    }
    @FXML
    public void onGearDown(ActionEvent actionEvent) {
        if (activeGearbox != null) activeGearbox.gearDown();
        System.out.println("One gear down!");
    }

    private ClutchController clutchController;

    public void setClutchController(ClutchController clutchController) {
        this.clutchController = clutchController;
    }
    public Gearbox getGearboxFromInput() {
        int gears = Integer.parseInt(gearsTextField.getText());
        String name = gearboxNameTextField.getText();
        double weight = Double.parseDouble(gearboxWeightTextField.getText());
        double price = Double.parseDouble(gearboxPriceTextField.getText());
        Clutch clutch = clutchController.getClutchFromInput();

        return new Gearbox(gears, name, weight, price, clutch);
    }
}
