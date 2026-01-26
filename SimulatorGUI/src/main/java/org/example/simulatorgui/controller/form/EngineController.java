package org.example.simulatorgui.controller.form;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.simulatorgui.model.components.CarComponentsRepository;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.util.Utils;

public class EngineController {
    @FXML private TextField engineNameTextField;
    @FXML private TextField enginePriceTextField;
    @FXML private TextField engineWeightTextField;
    @FXML private TextField engineRPMTextField;
    @FXML private Button confirmEngineButton;

    private final CarComponentsRepository carComponentsRepository = CarComponentsRepository.getInstance();
    private NewCarController addCarController;
    private ComponentsController carComponentsController;

    public void setAddCarController(NewCarController addCarController) { this.addCarController = addCarController; }
    public void setCarComponentsController(ComponentsController carComponentsController) { this.carComponentsController = carComponentsController; }

    @FXML private void initialize() { Platform.runLater(this::validateInput); }
    private void validateInput() {
        enginePriceTextField.setTextFormatter(Utils.createDecimalTextFormatter());
        engineWeightTextField.setTextFormatter(Utils.createDecimalTextFormatter());
        engineRPMTextField.setTextFormatter(Utils.createIntegerTextFormatter());
        confirmEngineButton.disableProperty().bind(
            engineNameTextField.textProperty().isEmpty()
                .or(enginePriceTextField.textProperty().isEmpty())
                .or(engineWeightTextField.textProperty().isEmpty())
                .or(engineRPMTextField.textProperty().isEmpty())
        );
    }

    public Engine getEngineFromInput() {
        String name = engineNameTextField.getText();
        if (carComponentsRepository.isDuplicateEngineName(name)) {
            Utils.showDuplicateAlert("engine name", name);
            return null;
        }
        try {
            double weight = Double.parseDouble(engineWeightTextField.getText());
            double price = Double.parseDouble(enginePriceTextField.getText());
            int maxRPM = Integer.parseInt(engineRPMTextField.getText());
            return new Engine(maxRPM, name, weight, price);
        } catch (NumberFormatException e) { throw new IllegalStateException("Input value is incorrect!"); }
    }

    // ===================== ACTIONS =====================
    @FXML private void onConfirm() {
        Engine engine = getEngineFromInput();
        if (engine == null) return;
        carComponentsRepository.addEngineIfAbsent(engine);
        carComponentsController.getEngineComboBox().getSelectionModel().select(engine);
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
    @FXML private void onCancel() {
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
}
