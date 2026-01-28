package org.example.simulatorgui.controller.form;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.repo.CarRepository;
import org.example.simulatorgui.model.components.Engine;
import org.example.simulatorgui.model.components.Gearbox;
import org.example.simulatorgui.model.util.Utils;

import java.io.IOException;
import java.util.Optional;

public class NewCarController {
    @FXML private TextField carModelTextField;
    @FXML private TextField carPlateNumberTextField;
    @FXML private TextField carWeightTextField;
    @FXML private TextField carMaxSpeedTextField;
    @FXML private Button confirmCarButton;

    @FXML private VBox carBasicInfoForm;
    @FXML private VBox carComponentsForm;
    @FXML private VBox engineGearboxForm;
    @FXML private VBox clutchForm;

    private ComponentsController carComponentsController;
    private CarRepository carRepository;

    public VBox getCarBasicInfoForm() { return carBasicInfoForm; }
    public VBox getCarComponentsForm() { return carComponentsForm; }
    public VBox getEngineGearboxForm() { return engineGearboxForm; }
    public VBox getClutchForm() { return clutchForm; }

    public void setCarRepository(CarRepository carRepository) { this.carRepository = carRepository; }
    public void setCarComponentsController(ComponentsController carComponentsController) {
        this.carComponentsController = carComponentsController;
        initializeValidation();
    }

    // ===================== INITIALIZATION =====================
    @FXML private void initialize() { Platform.runLater(this::initializeValidation); }
    private void initializeValidation() {
        if (carComponentsController == null) return;
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

    private Optional<Car> getCarFromInput() {
        if (carComponentsController == null || carRepository == null) throw new IllegalStateException("Controller not fully initialized!");
        Engine engine = carComponentsController.getEngineFromInput();
        Gearbox gearbox = carComponentsController.getGearboxFromInput();
        String model = carModelTextField.getText().trim();
        String plateNumber = carPlateNumberTextField.getText().trim();

        if (carRepository.isDuplicatePlate(plateNumber)) {
            Utils.showDuplicateAlert("license plate", plateNumber);
            return Optional.empty();
        }
        try {
            double weight = Double.parseDouble(carWeightTextField.getText());
            int vMax = Integer.parseInt(carMaxSpeedTextField.getText());
            return Optional.of(new Car(plateNumber, model, weight, vMax, engine, gearbox));
        } catch (NumberFormatException e) { throw new IllegalStateException("Input value is incorrect!"); } catch (
                InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // ===================== ACTIONS =====================
    public <T> T showForm(String view, VBox toShow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/form/" + view));
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

    @FXML private void onCancel() {
        closeForm(carComponentsForm);
        closeWindow();
    }
    @FXML private void onConfirm() {
        Optional<Car> carOpt = getCarFromInput();
        if (carOpt.isEmpty()) return;
        Car car = carOpt.get();
        carRepository.addCar(car);
        closeWindow();
    }
}
