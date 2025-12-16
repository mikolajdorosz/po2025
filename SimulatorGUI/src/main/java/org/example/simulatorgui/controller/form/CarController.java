package org.example.simulatorgui.controller.form;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

import java.io.IOException;
import java.util.function.Consumer;

public class CarController {

    @FXML private TextField carModelTextField;
    @FXML private TextField carPlateNumberTextField;
    @FXML private TextField carWeightTextField;
    @FXML private TextField carVMAXTextField;
    @FXML private ComboBox<Engine> engineComboBox;
    @FXML private ComboBox<Gearbox> gearboxComboBox;

    public ComboBox<Engine> getEngineComboBox() {
        return engineComboBox;
    }
    public ComboBox<Gearbox> getGearboxComboBox() {
        return gearboxComboBox;
    }

    private AddCarController addCarController;
    private EngineController engineController;
    private GearboxController gearboxController;

    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }
    public void setEngineController(EngineController engineController) {
        this.engineController = engineController;
    }
    public void setGearboxController(GearboxController gearboxController) {
        this.gearboxController = gearboxController;
    }

    @FXML
    private void initialize() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Car getCarFromInput(Engine engine, Gearbox gearbox) {
        String model = carModelTextField.getText().trim();
        String plateNumber = carPlateNumberTextField.getText().trim();
        Position position = new Position(0, 0);
        double weight;
        int vMax;
        try {
            weight = Double.parseDouble(carWeightTextField.getText());
            vMax = Integer.parseInt(carVMAXTextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Input value is incorrect!");
        }
        return new Car(plateNumber, model, weight, vMax, position, engine, gearbox);
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onCreateEngine() throws IOException {
        EngineController controller = addCarController.showForm(
                "engine_form.fxml",
                addCarController.getEngineFormContainer(),
                addCarController.getCarFormContainer(),
                c -> {
                    c.setOnCreated(engine -> {
                        engineComboBox.getItems().add(engine);
                        engineComboBox.getSelectionModel().select(engine);
                    });
                    c.setOnClose(() -> addCarController.getCarFormContainer().setDisable(false));
                }
        );
    }
    @FXML
    private void onCreateGearbox() throws IOException {
        GearboxController controller = addCarController.showForm(
                "gearbox_form.fxml",
                addCarController.getGearboxFormContainer(),
                addCarController.getCarFormContainer(),
                c -> {
                    c.setOnCreated(gearbox -> {
                        gearboxComboBox.getItems().add(gearbox);
                        gearboxComboBox.getSelectionModel().select(gearbox);
                    });
                    c.setOnClose(() -> addCarController.getCarFormContainer().setDisable(false));
                }
        );
    }

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
    private void onCancel() {
        addCarController.closeForm(addCarController.getCarFormContainer());
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) addCarController.getCarFormContainer().getScene().getWindow();
        stage.close();
    }



















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





    private void refresh() {
        carModelTextField.setText(activeCar.getModel());
        carPlateNumberTextField.setText(activeCar.getPlateNumber());
        carWeightTextField.setText(String.valueOf(activeCar.getWeight()));
        carVMAXTextField.setText(String.valueOf(activeCar.getCurrentV()));
    }




    // === Callback ===
    private Consumer<Car> onCarCreated;

    public void setOnCarCreated(Consumer<Car> callback) {
        this.onCarCreated = callback;
    }
}
