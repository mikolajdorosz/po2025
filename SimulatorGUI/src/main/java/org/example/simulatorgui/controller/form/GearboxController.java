package org.example.simulatorgui.controller.form;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

import java.io.IOException;
import java.util.function.Consumer;

public class GearboxController {
    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearboxPriceTextField;
    @FXML private TextField gearboxWeightTextField;
    @FXML private TextField gearsTextField;
    @FXML private ToggleGroup gearboxTypeToggleGroup;
    @FXML private ToggleButton typeAutoToggleButton;
    @FXML private ToggleButton typeManualToggleButton;
    private String gearboxType;
    @FXML private HBox clutchComboBoxContainer;
    @FXML private ComboBox<Clutch> clutchComboBox;
    public ComboBox<Clutch> getClutchComboBox() {
        return clutchComboBox;
    }


    private AddCarController addCarController;
    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    private CarController carController;
    public void setCarController(CarController carController) {
        this.carController = carController;
    }


    @FXML
    private void initialize() {
        gearboxTypeToggleGroup = new ToggleGroup();

        typeAutoToggleButton.setToggleGroup(gearboxTypeToggleGroup);
        typeManualToggleButton.setToggleGroup(gearboxTypeToggleGroup);

        typeAutoToggleButton.setUserData("automatic");
        typeManualToggleButton.setUserData("manual");

        typeAutoToggleButton.setSelected(true);
        gearboxType = "automatic";

        gearboxTypeToggleGroup.selectedToggleProperty().addListener(
                (obs, oldToggle, newToggle) -> {
                    if (newToggle != null) {
                        gearboxType = newToggle.getUserData().toString();
                    }
                    updateToggleStyles();
                    System.out.println("Selected gearbox type: " + gearboxType);

                }
        );

        clutchComboBoxContainer.setVisible(false);
        clutchComboBoxContainer.setManaged(false);

        // słuchacz zmian ToggleGroup
        gearboxTypeToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                gearboxType = newToggle.getUserData().toString();

                boolean isManual = "manual".equals(gearboxType);
                clutchComboBoxContainer.setVisible(isManual);
                clutchComboBoxContainer.setManaged(isManual);

                // opcjonalnie odśwież style toggle buttons
                updateToggleStyles();
            }
        });
    }
    private void updateToggleStyles() {
        styleToggle(typeAutoToggleButton, typeAutoToggleButton.isSelected());
        styleToggle(typeManualToggleButton, typeManualToggleButton.isSelected());
    }

    private void styleToggle(ToggleButton toggle, boolean selected) {
        toggle.setStyle(selected
                ? """
          -fx-background-insets: 0;
          -fx-focus-color: transparent;
          -fx-faint-focus-color: transparent;
          -fx-border-color: #3b82f6;
          -fx-border-width: 2;
          -fx-border-radius: 12;
          -fx-background-radius: 12;
          -fx-background-color: #ffffff;
          -fx-padding: 14 20;
          -fx-font-size: 14px;
          -fx-font-weight: 600;
          -fx-text-fill: #1f2937;
          -fx-cursor: hand;
          """
                : """
          -fx-background-insets: 0;
          -fx-focus-color: transparent;
          -fx-faint-focus-color: transparent;
          -fx-border-color: #cbd5e1;
          -fx-border-width: 1;
          -fx-border-radius: 12;
          -fx-background-radius: 12;
          -fx-background-color: #ffffff;
          -fx-padding: 14 20;
          -fx-font-size: 14px;
          -fx-font-weight: 600;
          -fx-text-fill: #1f2937;
          -fx-cursor: hand;
          """
        );
    }


    private void trackRadioButtons() {

        System.out.println(gearboxType);
    }

    // ===================== getGearbox =====================
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
                "clutch_form.fxml",
                addCarController.getClutchFormContainer(),
                addCarController.getGearboxFormContainer()
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
        carController.getGearboxComboBox().getItems().add(gearbox);
        carController.getGearboxComboBox().getSelectionModel().select(gearbox);
        addCarController.closeForm(addCarController.getGearboxFormContainer(), addCarController.getCarFormContainer());
    }
    @FXML
    private void onCancel() {
        addCarController.closeForm(addCarController.getGearboxFormContainer(), addCarController.getCarFormContainer());
    }












    public void addClutchToGearbox(Clutch clutch) {
        if (currentGearbox != null) {
            currentGearbox.setClutch(clutch);
        }
        if (clutchComboBox != null) {
            clutchComboBox.getItems().add(clutch);
            clutchComboBox.getSelectionModel().select(clutch);
        }
    }


    private Gearbox currentGearbox;
    @FXML private Button addClutchButton;


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

    }


}
