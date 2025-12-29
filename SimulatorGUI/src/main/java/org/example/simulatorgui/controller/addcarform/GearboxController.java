package org.example.simulatorgui.controller.addcarform;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

import java.io.IOException;

public class GearboxController {
    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearboxPriceTextField;
    @FXML private TextField gearboxWeightTextField;
    @FXML private TextField gearsTextField;
    @FXML private ToggleGroup gearboxTypeToggleGroup;
    @FXML private ToggleButton typeAutoToggleButton;
    @FXML private ToggleButton typeManualToggleButton;
    @FXML private ComboBox<Clutch> clutchComboBox;
    @FXML private HBox clutchComboBoxContainer;
    private String gearboxType;
    private AddCarController addCarController;
    private CarComponentsController carComponentsController;

    public ComboBox<Clutch> getClutchComboBox() {
        return clutchComboBox;
    }
    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    public void setCarComponentsController(CarComponentsController carComponentsController) {
        this.carComponentsController = carComponentsController;
    }

    @FXML
    private void initialize() {
        setupTypeToggling();
    }

    private void setupTypeToggling() {
        gearboxTypeToggleGroup = new ToggleGroup();
        typeAutoToggleButton.setToggleGroup(gearboxTypeToggleGroup);
        typeManualToggleButton.setToggleGroup(gearboxTypeToggleGroup);
        gearboxTypeToggleGroup.selectedToggleProperty().addListener(
                (obs, oldToggle, newToggle) -> {
                    if (newToggle == null) return;
                    gearboxType = newToggle.getUserData().toString();
                    if ("automatic".equals(gearboxType)) clutchComboBoxContainer.setDisable(true);
                    else clutchComboBoxContainer.setDisable(false);
                }
        );
    }

    public Gearbox getGearboxFromInput() {
        Clutch clutch = clutchComboBox.getValue();
        clutchComboBox.getItems().add(clutch);
        clutchComboBox.getSelectionModel().select(clutch);

        String name = gearboxNameTextField.getText();
        double weight, price;
        int gears;
        try {
            gears = Integer.parseInt(gearsTextField.getText());
            weight = Double.parseDouble(gearboxWeightTextField.getText());
            price = Double.parseDouble(gearboxPriceTextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Input value is incorrect!");
        }
        if ("manual".equals(gearboxType)) {
            return new Gearbox(gears, gearboxType, name, weight, price, clutch);
        } else {
            return new Gearbox(gears, gearboxType, name, weight, price);
        }
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onCreateClutch() throws IOException {
        ClutchController controller = addCarController.showForm(
                "clutch-form.fxml",
                addCarController.getClutchForm(),
                addCarController.getEngineGearboxForm()
        );
        controller.setAddCarController(addCarController);
        controller.setGearboxController(this);
    }
    @FXML
    private void onDeleteClutch() {
        Clutch selected = clutchComboBox.getValue();
        if (selected != null) {
            clutchComboBox.getItems().remove(selected);
        }
    }

    @FXML
    private void onConfirm() {
        Gearbox gearbox = getGearboxFromInput();
        carComponentsController.getGearboxComboBox().getItems().add(gearbox);
        carComponentsController.getGearboxComboBox().getSelectionModel().select(gearbox);
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
    @FXML
    private void onCancel() {
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
}
