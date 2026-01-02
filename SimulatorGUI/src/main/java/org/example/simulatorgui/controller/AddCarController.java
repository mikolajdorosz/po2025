package org.example.simulatorgui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.addcarform.CarComponentsController;
import simulator.*;

import java.io.IOException;

public class AddCarController {
    @FXML private TextField carModelTextField;
    @FXML private TextField carPlateNumberTextField;
    @FXML private TextField carWeightTextField;
    @FXML private TextField carMaxSpeedTextField;
    @FXML private Button confirmCarButton;
    @FXML private VBox carBasicInfoForm;
    @FXML private VBox carComponentsForm;
    @FXML private VBox engineGearboxForm;
    @FXML private VBox clutchForm;
    private RaceSetupController raceSetupController;
    private CarComponentsController carComponentsController;

    public VBox getCarBasicInfoForm() { return carBasicInfoForm; }
    public VBox getCarComponentsForm() { return carComponentsForm; }
    public VBox getEngineGearboxForm() { return engineGearboxForm; }
    public VBox getClutchForm() { return clutchForm; }
    public void setRaceSetupController(RaceSetupController raceSetupController) { this.raceSetupController = raceSetupController; }
    public void setCarComponentsController(CarComponentsController carComponentsController) { this.carComponentsController = carComponentsController; }

    @FXML
    private void initialize() { Platform.runLater(() -> validateInput()); }
    private void validateInput() {
        carWeightTextField.setTextFormatter(Utils.createDecimalTextFormatter());
        carMaxSpeedTextField.setTextFormatter(Utils.createIntegerTextFormatter());
        confirmCarButton.disableProperty().bind(
            carModelTextField.textProperty().isEmpty()
                .or(carPlateNumberTextField.textProperty().isEmpty())
                .or(carWeightTextField.textProperty().isEmpty())
                .or(carMaxSpeedTextField.textProperty().isEmpty())
                .or(carComponentsController.getEngineComboBox().valueProperty().isNull())
                .or(carComponentsController.getGearboxComboBox().valueProperty().isNull())
        );
    }

    public Car getCarFromInput() {
        Engine engine = carComponentsController.getEngineFromInput();
        Gearbox gearbox = carComponentsController.getGearboxFromInput();
        String model = carModelTextField.getText().trim();
        String plateNumber = carPlateNumberTextField.getText().trim();
        boolean duplicate = raceSetupController.getRaceCars().stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plateNumber))
                        || raceSetupController.getStoredCars().stream().anyMatch(c -> c.getPlateNumber().equalsIgnoreCase(plateNumber));
        if (duplicate) {
            Utils.showDuplicateAlert("license plate", plateNumber);
            return null;
        }
        Position position = new Position(0, 0);
        double weight;
        int vMax;
        try {
            weight = Double.parseDouble(carWeightTextField.getText());
            vMax = Integer.parseInt(carMaxSpeedTextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Input value is incorrect!");
        }
        return new Car(plateNumber, model, weight, vMax, position, engine, gearbox);
    }
    // ===================== ACTIONS =====================
    public <T> T showForm(String view, VBox toShow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/addcarform/" + view));
        Node form = loader.load();
        T controller = loader.getController();
        toShow.getChildren().setAll(form); // Placing form in add_car view
        return controller;
    }
    public <T> T showForm(String view, VBox toShow, VBox fToDisable) throws IOException {
        T controller = showForm(view, toShow);
        toShow.setVisible(true);
        toShow.setManaged(true);
        if (fToDisable != null) fToDisable.setDisable(true);
        return controller;
    }
    public <T> T showForm(String view, VBox toShow, VBox fToDisable, VBox sToDisable) throws IOException {
        T controller = showForm(view, toShow, fToDisable);
        if (sToDisable != null) sToDisable.setDisable(true);
        return controller;
    }
    public void closeForm(VBox toClose) { toClose.getChildren().clear(); }
    public void closeForm(VBox toClose, VBox fToEnable) {
        closeForm(toClose);
        toClose.setVisible(false);
        toClose.setManaged(false);
        fToEnable.setDisable(false);
    }
    public void closeForm(VBox toClose, VBox fToEnable, VBox sToEnable) {
        closeForm(toClose, fToEnable);
        sToEnable.setDisable(false);
    }
    public void closeWindow() {
        Stage stage = (Stage) carComponentsForm.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        closeForm(carComponentsForm);
        closeWindow();
    }
    @FXML
    private void onConfirm() {
        Car car = getCarFromInput();
        if (car == null) return;
        raceSetupController.getStoredCars().add(car);
        raceSetupController.getStoredCarsComboBox().getSelectionModel().select(car);
        closeWindow();
    }
}
