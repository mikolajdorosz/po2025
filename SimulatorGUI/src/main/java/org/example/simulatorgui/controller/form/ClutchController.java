package org.example.simulatorgui.controller.form;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

public class ClutchController {
    @FXML
    private TextField clutchNameTextField;
    @FXML
    private TextField clutchPriceTextField;
    @FXML
    private TextField clutchWeightTextField;
    @FXML
    private TextField clutchStateTextField;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

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

    private AddCarController parentController;
    public void setParentController(AddCarController controller) {
        this.parentController = controller;
    }

    public Clutch getClutchFromInput() {
        String name = clutchNameTextField.getText();
        double weight = Double.parseDouble(clutchWeightTextField.getText());
        double price = Double.parseDouble(clutchPriceTextField.getText());
        return new Clutch(name, weight, price);
    }

    @FXML
    private void onConfirm() {
        Clutch clutch = new Clutch();
        clutch.setName(clutchNameTextField.getText());
        clutch.setPrice(Double.parseDouble(clutchPriceTextField.getText()));

        if (parentController != null) {
            parentController.onClutchCreated(clutch);
        }
    }
    @FXML
    private void onCancel() {
        if (parentController != null) {
            parentController.closeSideForm("clutch");
        }
    }
}
