package org.example.simulatorgui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.example.simulatorgui.controller.form.CarController;
import org.example.simulatorgui.controller.form.ClutchController;
import org.example.simulatorgui.controller.form.EngineController;
import org.example.simulatorgui.controller.form.GearboxController;
import simulator.*;

import java.io.IOException;
import java.util.function.Consumer;

public class AddCarController {

    @FXML private VBox carFormContainer;
    public VBox getCarFormContainer() {
        return carFormContainer;
    }

    @FXML private VBox engineFormContainer;
    public VBox getEngineFormContainer() {
        return engineFormContainer;
    }

    @FXML private VBox gearboxFormContainer;
    public VBox getGearboxFormContainer() {
        return gearboxFormContainer;
    }

    @FXML private VBox clutchFormContainer;
    public VBox getClutchFormContainer() {
        return clutchFormContainer;
    }

//    @FXML private VBox carForm;
//    public VBox getCarForm() {
//        return carForm;
//    }
//
//    @FXML private VBox engineForm;
//    public VBox getEngineForm() {
//        return engineForm;
//    }
//
//    @FXML private VBox gearboxForm;
//    public VBox getGearboxForm() {
//        return gearboxForm;
//    }
//
//    @FXML private VBox clutchForm;
//    public VBox getClutchForm() {
//        return clutchForm;
//    }

    private CarController carController;
    private EngineController engineController;
    private GearboxController gearboxController;
    private ClutchController clutchController;

    @FXML
    private void initialize() {
        try {
            showForm("car_form.fxml", carFormContainer, controller -> {
                carController = (CarController) controller;
                carController.setAddCarController(this);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== ACTIONS =====================
    public <T> T showForm(String view, VBox toShow, Consumer<T> controllerConsumer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/form/" + view));
        Node form = loader.load();
        T controller = loader.getController();
        if (controllerConsumer != null)  controllerConsumer.accept(controller);
        toShow.getChildren().setAll(form); // Placing form in add_car view
        return controller;
    }
    public <T> T showForm(String view, VBox toShow, VBox toDisable, Consumer<T> controllerConsumer) throws IOException {
        T controller = showForm(view, toShow, controllerConsumer);
        toShow.setVisible(true);
        toShow.setManaged(true);
        if (toDisable != null) toDisable.setDisable(true);
        return controller;
    }

    public void closeForm(VBox toClose) {
        toClose.getChildren().clear();
    }
    public void closeForm(VBox toClose, VBox toEnable) {
        closeForm(toClose);
        toClose.setVisible(false);
        toClose.setManaged(false);
        toEnable.setDisable(false);
    }



    // === Callback ===
    private Consumer<Car> onCarCreated;
    public void setOnCarCreated(Consumer<Car> callback) {
        this.onCarCreated = callback;
    }


    // ================= CALLBACKS =================
    public void onEngineCreated(Engine engine) {
        if (carController != null) {
            carController.getEngineComboBox().getItems().add(engine);
            carController.getEngineComboBox().getSelectionModel().select(engine);
        }
    }

    public void onGearboxCreated(Gearbox gearbox) {
        if (carController != null) {
            carController.getGearboxComboBox().getItems().add(gearbox);
            carController.getGearboxComboBox().getSelectionModel().select(gearbox);
        }
    }

    public void onClutchCreated(Clutch clutch) {
        if (gearboxController != null) {
            gearboxController.getClutchComboBox().getItems().add(clutch);
            gearboxController.getClutchComboBox().getSelectionModel().select(clutch);

        }
    }
}
