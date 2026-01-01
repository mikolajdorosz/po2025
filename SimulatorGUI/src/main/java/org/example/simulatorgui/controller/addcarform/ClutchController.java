package org.example.simulatorgui.controller.addcarform;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

public class ClutchController {
    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchPriceTextField;
    @FXML private TextField clutchWeightTextField;
    @FXML private Button confirmClutchButton;
    private AddCarController addCarController;
    private GearboxController gearboxController;

    public void setAddCarController(AddCarController addCarController) { this.addCarController = addCarController; }
    public void setGearboxController(GearboxController gearboxController) { this.gearboxController = gearboxController; }

    @FXML
    private void initialize() { Platform.runLater(() -> validateInput()); }
    private void validateInput() {
        clutchPriceTextField.setTextFormatter(Utils.createDecimalTextFormatter());
        clutchWeightTextField.setTextFormatter(Utils.createDecimalTextFormatter());
        confirmClutchButton.disableProperty().bind(
            clutchNameTextField.textProperty().isEmpty()
                .or(clutchPriceTextField.textProperty().isEmpty())
                .or(clutchWeightTextField.textProperty().isEmpty())
        );
    }

    public Clutch getClutchFromInput() {
        String name = clutchNameTextField.getText();
        double weight, price;
        try {
            weight = Double.parseDouble(clutchWeightTextField.getText());
            price = Double.parseDouble(clutchPriceTextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Input value is incorrect!");
        }
        return new Clutch(name, weight, price);
    }
    // ===================== ACTIONS =====================
    @FXML
    private void onConfirm() {
        Clutch clutch = getClutchFromInput();
        gearboxController.getClutchComboBox().getItems().add(clutch);
        gearboxController.getClutchComboBox().getSelectionModel().select(clutch);
        addCarController.closeForm(addCarController.getClutchForm(), addCarController.getEngineGearboxForm());
    }
    @FXML
    private void onCancel() {
        addCarController.closeForm(addCarController.getClutchForm(), addCarController.getEngineGearboxForm());
    }
}
