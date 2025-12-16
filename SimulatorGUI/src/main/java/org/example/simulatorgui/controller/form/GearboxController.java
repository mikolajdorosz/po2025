package org.example.simulatorgui.controller.form;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

public class GearboxController {
    @FXML private ComboBox<String> typeComboBox; // "Manual", "Automatic"
    @FXML private ComboBox<Clutch> clutchComboBox;
    @FXML private Button addClutchButton;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    @FXML
    private TextField gearboxNameTextField;
    @FXML
    private TextField gearboxPriceTextField;
    @FXML
    private TextField gearboxWeightTextField;
    @FXML
    private TextField gearsTextField;
    @FXML
    private Button gearUpButton;
    @FXML
    private Button gearDownButton;

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

    private ClutchController clutchController;

    public void setClutchController(ClutchController clutchController) {
        this.clutchController = clutchController;
    }
    public Gearbox getGearboxFromInput() {
        int gears = Integer.parseInt(gearsTextField.getText());
        String name = gearboxNameTextField.getText();
        double weight = Double.parseDouble(gearboxWeightTextField.getText());
        double price = Double.parseDouble(gearboxPriceTextField.getText());
        Clutch clutch = clutchController.getClutchFromInput();

        return new Gearbox(gears, name, weight, price, clutch);
    }

    private AddCarController parentController;
    private Gearbox currentGearbox;

    public void setParentController(AddCarController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void onCancel(ActionEvent actionEvent) {
        if (parentController != null) {
            parentController.closeSideForm("gearbox");
        }
    }
    @FXML
    private void onConfirm(ActionEvent actionEvent) {
        currentGearbox = new Gearbox();
        currentGearbox.setType(typeComboBox.getValue());
        currentGearbox.setClutch(clutchComboBox.getValue());

        if (parentController != null) {
            parentController.onGearboxCreated(currentGearbox);
        }
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

    @FXML
    private void onAddClutch() throws Exception {
        // wywołanie AddCarController aby otworzyć trzeci panel
        parentController.onAddClutch();
    }

    @FXML
    private void initialize() {
        typeComboBox.getItems().addAll("Manual", "Automatic");
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean manual = "Manual".equals(newVal);
            clutchComboBox.setDisable(!manual);
            addClutchButton.setDisable(!manual);
        });
    }
}
//TODO widok parametrów wybranego samochodu
