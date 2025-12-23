package org.example.simulatorgui.controller.form;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.AddCarController;
import org.example.simulatorgui.controller.MenuController;
import simulator.*;

import java.io.IOException;
import java.util.function.Consumer;

public class CarController {
    @FXML private TextField carModelTextField;
    @FXML private TextField carPlateNumberTextField;
    @FXML private TextField carWeightTextField;
    @FXML private TextField carVMAXTextField;
    @FXML private ComboBox<Engine> engineComboBox;
    public ComboBox<Engine> getEngineComboBox() {
        return engineComboBox;
    }
    @FXML private ComboBox<Gearbox> gearboxComboBox;
    public ComboBox<Gearbox> getGearboxComboBox() {
        return gearboxComboBox;
    }


    private MenuController menuController;
    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }
    private AddCarController addCarController;
    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }


    @FXML
    private void initialize() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== getCar =====================
    public Car getCarFromInput() {
        Engine engine = engineComboBox.getValue();
        engineComboBox.getItems().add(engine);
        engineComboBox.getSelectionModel().select(engine);

        Gearbox gearbox = gearboxComboBox.getValue();
        gearboxComboBox.getItems().add(gearbox);
        gearboxComboBox.getSelectionModel().select(gearbox);

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
    private void onNewEngine() throws IOException {
        EngineController controller = addCarController.showForm(
                "engine_form.fxml",
                addCarController.getEngineFormContainer(),
                addCarController.getCarFormContainer()
        );
        controller.setAddCarController(addCarController);
        controller.setCarController(this);
    }
    @FXML
    private void onDeleteEngine() {
        Engine selected = engineComboBox.getValue();
        if (selected != null) {
            engineComboBox.getItems().remove(selected);
        }
    }
    @FXML
    private void onNewGearbox() throws IOException {
        GearboxController controller = addCarController.showForm(
                "gearbox_form.fxml",
                addCarController.getGearboxFormContainer(),
                addCarController.getCarFormContainer()
        );
        controller.setAddCarController(addCarController);
        controller.setCarController(this);
    }
    @FXML
    private void onDeleteGearbox() {
        Gearbox selected = gearboxComboBox.getValue();
        if (selected != null) {
            gearboxComboBox.getItems().remove(selected);
        }
    }

    @FXML
    private void onConfirm() {
        Car car = getCarFromInput();
        menuController.getStoredCarsList().add(car);
        menuController.getStoredCarsComboBox().getSelectionModel().select(car);
        addCarController.closeWindow();
    }
    @FXML
    private void onCancel() {
        addCarController.closeForm(addCarController.getCarFormContainer());
        addCarController.closeWindow();
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


}
