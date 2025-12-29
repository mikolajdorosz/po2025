package org.example.simulatorgui.controller.addcarform;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

public class EngineController {
    @FXML private TextField engineNameTextField;
    @FXML private TextField enginePriceTextField;
    @FXML private TextField engineWeightTextField;
    @FXML private TextField engineRPMTextField;
    private AddCarController addCarController;
    private CarComponentsController carComponentsController;

    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    public void setCarComponentsController(CarComponentsController carComponentsController) {
        this.carComponentsController = carComponentsController;
    }

    public Engine getEngineFromInput() {
        String name = engineNameTextField.getText();
        double weight, price;
        int maxRPM;
        try {
            weight = Double.parseDouble(engineWeightTextField.getText());
            price = Double.parseDouble(enginePriceTextField.getText());
            maxRPM = Integer.parseInt(engineRPMTextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Input value is incorrect!");
        }
        return new Engine(maxRPM, name, weight, price);
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onConfirm() {
        Engine engine = getEngineFromInput();
        carComponentsController.getEngineComboBox().getItems().add(engine);
        carComponentsController.getEngineComboBox().getSelectionModel().select(engine);
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
    @FXML
    private void onCancel() {
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
}
