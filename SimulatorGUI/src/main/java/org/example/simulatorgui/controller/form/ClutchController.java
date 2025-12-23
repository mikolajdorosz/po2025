package org.example.simulatorgui.controller.form;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

import java.util.function.Consumer;

public class ClutchController {
    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchPriceTextField;
    @FXML private TextField clutchWeightTextField;


    private AddCarController addCarController;
    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    private GearboxController gearboxController;
    public void setGearboxController(GearboxController gearboxController) {
        this.gearboxController = gearboxController;
    }

    // ===================== getClutch =====================
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
        addCarController.closeForm(addCarController.getClutchFormContainer() ,addCarController.getGearboxFormContainer());
    }
    @FXML
    private void onCancel() {
        addCarController.closeForm(addCarController.getClutchFormContainer() ,addCarController.getGearboxFormContainer());
    }









    @FXML
    public void onPressClutch(ActionEvent actionEvent) {
        System.out.println("Clutch pressed!");
    }
    @FXML
    public void onReleaseClutch(ActionEvent actionEvent) {
        System.out.println("Clutch released!");
    }
}
