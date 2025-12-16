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

    private Consumer<Engine> onCreated;
    private Runnable onClose;

    public void setOnCreated(Consumer<Engine> callback) {
        this.onCreated = callback;
    }
    public void setOnClose(Runnable callback) { this.onClose = callback; }

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
        if (onCreated != null) onCreated.accept(engine);

        // close this form
        VBox container = (VBox) engineNameTextField.getScene().lookup("#engineFormContainer");
        if (container != null) {
            container.getChildren().clear();
            container.setVisible(false);
            container.setManaged(false);
        }

        if (onClose != null) onClose.run(); // enable carForm
    }
    @FXML
    private void onCancel() {
        VBox container = (VBox) engineNameTextField.getScene().lookup("#engineFormContainer");
        if (container != null) {
            container.getChildren().clear();
            container.setVisible(false);
            container.setManaged(false);
        }

        if (onClose != null) onClose.run(); // enable carForm
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
