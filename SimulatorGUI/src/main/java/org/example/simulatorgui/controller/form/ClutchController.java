package org.example.simulatorgui.controller.form;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.simulatorgui.repo.CarComponentsRepository;
import org.example.simulatorgui.model.components.Clutch;
import org.example.simulatorgui.model.util.Utils;

public class ClutchController {
    @FXML private TextField clutchNameTextField;
    @FXML private TextField clutchPriceTextField;
    @FXML private TextField clutchWeightTextField;
    @FXML private Button confirmClutchButton;

    private final CarComponentsRepository carComponentsRepository = CarComponentsRepository.getInstance();
    private NewCarController addCarController;
    private GearboxController gearboxController;

    public void setAddCarController(NewCarController addCarController) { this.addCarController = addCarController; }
    public void setGearboxController(GearboxController gearboxController) { this.gearboxController = gearboxController; }

    @FXML private void initialize() { Platform.runLater(this::validateInput); }
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
        if (carComponentsRepository.isDuplicateClutchName(name)) {
            Utils.showDuplicateAlert("clutch name", name);
            return null;
        }
        try {
            double weight = Double.parseDouble(clutchWeightTextField.getText());
            double price = Double.parseDouble(clutchPriceTextField.getText());
            return new Clutch(name, weight, price);
        } catch (NumberFormatException e) { throw new IllegalStateException("Input value is incorrect!"); }
    }

    // ===================== ACTIONS =====================
    @FXML private void onConfirm() {
        Clutch clutch = getClutchFromInput();
        if (clutch == null) return;
        CarComponentsRepository.getInstance().addClutchIfAbsent(clutch);
        carComponentsRepository.addClutchIfAbsent(clutch);
        gearboxController.getClutchComboBox().getSelectionModel().select(clutch);
        addCarController.closeForm(addCarController.getClutchForm(), addCarController.getEngineGearboxForm());
    }
    @FXML private void onCancel() {
        addCarController.closeForm(addCarController.getClutchForm(), addCarController.getEngineGearboxForm());
    }
}
