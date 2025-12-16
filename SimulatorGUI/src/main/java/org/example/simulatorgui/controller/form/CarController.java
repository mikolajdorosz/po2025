package org.example.simulatorgui.controller.form;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

import java.io.IOException;
import java.util.function.Consumer;

public class CarController {
    public ComboBox<Engine> engineComboBox;
    public Button addEngineButton;
    public ComboBox<Gearbox> gearboxComboBox;
    public Button addGearboxButton;

    private AddCarController parentController;
    public void setParentController(AddCarController parent) {
        this.parentController = parent;
    }

    @FXML
    private TitledPane carPane;
    @FXML
    private TextField carModelTextField;
    @FXML
    private TextField carPlateNumberTextField;
    @FXML
    private TextField carWeightTextField;
    @FXML
    private TextField carVMAXTextField;

    @FXML
    private Button startCarButton;
    @FXML
    private Button turnOffCarButton;

    private Car activeCar;

    public void setActiveCar(Car car) {
        this.activeCar = car;
        System.out.println("Active car set to: " + car);
    }

    public Car getActiveCar() {
        return activeCar;
    }

    @FXML
    public void onStartCar() {
        if (activeCar != null) activeCar.start();
        refresh();
    }

    @FXML
    public void onTurnOffCar() {
        if (activeCar != null) activeCar.turnOff();
        refresh();
    }

    private EngineController engineController;
    private GearboxController gearboxController;

    public void setComponentsControllers(EngineController engineController, GearboxController gearboxController) {
        this.engineController = engineController;
        this.gearboxController = gearboxController;
    }

    public Car getCarFromInput(Engine engine, Gearbox gearbox) {
        String model = carModelTextField.getText().trim();
        String plateNumber = carPlateNumberTextField.getText().trim();
        double weight;
        int vMax;
        try {
            weight = Double.parseDouble(carWeightTextField.getText());
            vMax = Integer.parseInt(carVMAXTextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Niepoprawne dane liczbowe samochodu");
        }
        Position position = new Position(0, 0);

        return new Car(plateNumber, model, weight, vMax, position, engine, gearbox);
    }

    private void refresh() {
        carModelTextField.setText(activeCar.getModel());
        carPlateNumberTextField.setText(activeCar.getPlateNumber());
        carWeightTextField.setText(String.valueOf(activeCar.getWeight()));
        carVMAXTextField.setText(String.valueOf(activeCar.getCurrentV()));
    }

    @FXML
    private void onAddEngine() throws IOException {
        if (parentController != null) {
            parentController.showSideForm("/org/example/simulatorgui/view/form/engine_form.fxml", "engine");
        }
    }

    @FXML
    private void onAddGearbox() throws IOException {
        if (parentController != null) {
            parentController.showSideForm("/org/example/simulatorgui/view/form/gearbox_form.fxml", "gearbox");
        }
    }
    public ComboBox<Engine> getEngineComboBox() {
        return engineComboBox;
    }

    public ComboBox<Gearbox> getGearboxComboBox() {
        return gearboxComboBox;
    }

    // === Callback ===
    private Consumer<Car> onCarCreated;

    public void setOnCarCreated(Consumer<Car> callback) {
        this.onCarCreated = callback;
    }

    // ================= CONFIRM =================
    @FXML
    private void onConfirm() {
        Engine engine = engineComboBox.getValue();
        if (engine == null && engineController != null) {
            engine = engineController.getEngineFromInput();
            engineComboBox.getItems().add(engine);
            engineComboBox.getSelectionModel().select(engine);
        }

        Gearbox gearbox = gearboxComboBox.getValue();
        if (gearbox == null && gearboxController != null) {
            gearbox = gearboxController.getGearboxFromInput();
            gearboxComboBox.getItems().add(gearbox);
            gearboxComboBox.getSelectionModel().select(gearbox);
        }

        Car car = getCarFromInput(engine, gearbox);
        if (onCarCreated != null) onCarCreated.accept(car);

        closeWindow();
    }
    @FXML
    private VBox carFormContainer;

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) carFormContainer.getScene().getWindow();
        stage.close();
    }
}
