package org.example.simulatorgui.controller.addcarform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

public class ClutchController {
    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchPriceTextField;
    @FXML private TextField clutchWeightTextField;
    private AddCarController addCarController;
    private GearboxController gearboxController;

    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    public void setGearboxController(GearboxController gearboxController) {
        this.gearboxController = gearboxController;
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
