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

    private Consumer<Clutch> onCreated;
    private Runnable onClose;

    public void setOnCreated(Consumer<Clutch> callback) { this.onCreated = callback; }
    public void setOnClose(Runnable callback) { this.onClose = callback; }

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
        if (onCreated != null) onCreated.accept(clutch);

        VBox container = (VBox) clutchNameTextField.getScene().lookup("#clutchFormContainer");
        if (container != null) {
            container.getChildren().clear();
            container.setVisible(false);
            container.setManaged(false);
        }

        if (onClose != null) onClose.run(); // e.g., re-enable gearbox form
    }
    @FXML
    private void onCancel() {
        VBox container = (VBox) clutchNameTextField.getScene().lookup("#clutchFormContainer");
        if (container != null) {
            container.getChildren().clear();
            container.setVisible(false);
            container.setManaged(false);
        }
        if (onClose != null) onClose.run(); // enable carForm
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
