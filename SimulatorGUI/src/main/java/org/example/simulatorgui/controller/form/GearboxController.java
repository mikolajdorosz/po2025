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
    @FXML private ToggleGroup typeToggleGroup;
    @FXML private RadioButton typeManualRadioButton;
    @FXML private RadioButton typeAutoRadioButton;
    @FXML private HBox clutchComboBoxContainer;
    @FXML private ComboBox<Clutch> clutchComboBox;

    public ComboBox<Clutch> getClutchComboBox() {
        return clutchComboBox;
    }

    private Consumer<Gearbox> onCreated;
    private Runnable onClose;

    public void setOnCreated(Consumer<Gearbox> callback) {
        this.onCreated = callback;
    }
    public void setOnClose(Runnable callback) { this.onClose = callback; }


    private AddCarController addCarController;
    private ClutchController clutchController;
    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    public void setClutchController(ClutchController clutchController) {
        this.clutchController = clutchController;
    }

    private String gearboxType;

    @FXML
    private void initialize() {
        trackRadioButtons();
        Toggle selected = typeToggleGroup.getSelectedToggle();
        if (selected != null) {
            gearboxType = selected.getUserData().toString();
        }
    }

    public Gearbox getGearboxFromInput() {
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
            Clutch clutch = clutchController.getClutchFromInput();
            return new Gearbox(gears, name, weight, price, clutch);
        } else {
            return new Gearbox(gears, name, weight, price, gearboxType);
        }
    }

    private void trackRadioButtons() {
        typeToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) gearboxType = newToggle.getUserData().toString();
            if (gearboxType.equals("manual")) clutchComboBoxContainer.setVisible(true);
            else clutchComboBoxContainer.setVisible(false);
        });
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onCreateClutch() throws IOException {
        ClutchController controller = addCarController.showForm(
                "clutch_form.fxml",
                addCarController.getClutchFormContainer(),
                addCarController.getGearboxFormContainer(),
                c -> {
                    c.setOnCreated(clutch -> {
                        clutchComboBox.getItems().add(clutch);
                        clutchComboBox.getSelectionModel().select(clutch);
                    });
                    c.setOnClose(() -> addCarController.getGearboxFormContainer().setDisable(false));
                }
        );
    }


    @FXML
    private void onConfirm() {
        Gearbox gearbox = getGearboxFromInput();
        if (onCreated != null) onCreated.accept(gearbox);

        // close this form
        VBox container = (VBox) gearboxNameTextField.getScene().lookup("#gearboxFormContainer");
        if (container != null) {
            container.getChildren().clear();
            container.setVisible(false);
            container.setManaged(false);
        }

        if (onClose != null) onClose.run(); // enable carForm
    }
    @FXML
    private void onCancel() {
        VBox container = (VBox) gearboxNameTextField.getScene().lookup("#gearboxFormContainer");
        if (container != null) {
            container.getChildren().clear();
            container.setVisible(false);
            container.setManaged(false);
        }

        if (onClose != null) onClose.run(); // enable carForm
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
