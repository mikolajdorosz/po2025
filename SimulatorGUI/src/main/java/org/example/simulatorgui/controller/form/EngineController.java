package org.example.simulatorgui.controller.form;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

import java.util.function.Consumer;

public class EngineController {
    @FXML private TextField engineNameTextField;
    @FXML private TextField enginePriceTextField;
    @FXML private TextField engineWeightTextField;
    @FXML private TextField engineRPMTextField;


    private AddCarController addCarController;
    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    private CarController carController;
    public void setCarController(CarController carController) {
        this.carController = carController;
    }

    // ===================== getEngine =====================
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
        carController.getEngineComboBox().getItems().add(engine);
        carController.getEngineComboBox().getSelectionModel().select(engine);
        addCarController.closeForm(addCarController.getEngineFormContainer(), addCarController.getCarFormContainer());
    }
    @FXML
    private void onCancel() {
        addCarController.closeForm(addCarController.getEngineFormContainer(), addCarController.getCarFormContainer());
    }











    @FXML
    public void onSpeedUp(ActionEvent actionEvent) {
        System.out.println("Car is accelerating!");
    }
    @FXML
    public void onSlowDown(ActionEvent actionEvent) {
        System.out.println("Car is slowing down!");
    }
}
