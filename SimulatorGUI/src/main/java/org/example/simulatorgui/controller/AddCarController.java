package org.example.simulatorgui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.form.CarController;
import org.example.simulatorgui.controller.form.ClutchController;
import org.example.simulatorgui.controller.form.EngineController;
import org.example.simulatorgui.controller.form.GearboxController;
import simulator.*;

import java.io.IOException;
import java.util.function.Consumer;

public class AddCarController {

    // === FXML ===
    @FXML private VBox carFormContainer;
    @FXML private VBox engineFormContainer;
    @FXML private VBox gearboxFormContainer;
    @FXML private VBox carFormRoot;
    @FXML private StackPane sideFormContainer;
    @FXML private StackPane clutchFormContainer;

    // === Controllers ===
    private CarController carController;
    private EngineController engineController;
    private GearboxController gearboxController;
    private ClutchController clutchController;

    // === Callback ===
    private Consumer<Car> onCarCreated;
    public void setOnCarCreated(Consumer<Car> callback) {
        this.onCarCreated = callback;
    }

    public void showSideForm(String fxmlPath, String type) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent form = loader.load();

        switch (type) {
            case "engine" -> {
                engineController = loader.getController();
                engineController.setParentController(this);
                sideFormContainer.getChildren().setAll(form);
                sideFormContainer.setVisible(true);
                sideFormContainer.setManaged(true);
            }
            case "gearbox" -> {
                gearboxController = loader.getController();
                gearboxController.setParentController(this);
                sideFormContainer.getChildren().setAll(form);
                sideFormContainer.setVisible(true);
                sideFormContainer.setManaged(true);
            }
            case "clutch" -> {
                clutchController = loader.getController();
                clutchController.setParentController(this);
                clutchFormContainer.getChildren().setAll(form);
                clutchFormContainer.setVisible(true);
                clutchFormContainer.setManaged(true);
            }
        }

        // dezaktywacja paneli - poprawione
        carFormRoot.setDisable(!"clutch".equals(type));
        sideFormContainer.setDisable("clutch".equals(type));
        clutchFormContainer.setDisable(false); // zawsze aktywny, gdy otwierany
    }


    public void closeSideForm(String type) {
        if ("engine".equals(type) || "gearbox".equals(type)) {
            sideFormContainer.getChildren().clear();
            sideFormContainer.setVisible(false);
            sideFormContainer.setManaged(false);
            carFormRoot.setDisable(false);
        } else if ("clutch".equals(type)) {
            clutchFormContainer.getChildren().clear();
            clutchFormContainer.setVisible(false);
            clutchFormContainer.setManaged(false);
            sideFormContainer.setDisable(false);
            carFormRoot.setDisable(false);
        }
    }
    // ================= CALLBACKS =================
    public void onEngineCreated(Engine engine) {
        if (carController != null) {
            carController.getEngineComboBox().getItems().add(engine);
            carController.getEngineComboBox().getSelectionModel().select(engine);
        }
        closeSideForm("engine");
    }

    public void onGearboxCreated(Gearbox gearbox) {
        if (carController != null) {
            carController.getGearboxComboBox().getItems().add(gearbox);
            carController.getGearboxComboBox().getSelectionModel().select(gearbox);
        }
        closeSideForm("gearbox");
    }

    public void onClutchCreated(Clutch clutch) {
        if (gearboxController != null) {
            gearboxController.addClutchToGearbox(clutch);
        }
        closeSideForm("clutch");
    }

    // ================= BUTTON ACTIONS =================
    @FXML
    private void onAddEngine() throws IOException {
        showSideForm("/org/example/simulatorgui/view/form/engine_form.fxml", "engine");
    }

    @FXML
    private void onAddGearbox() throws IOException {
        showSideForm("/org/example/simulatorgui/view/form/gearbox_form.fxml", "gearbox");
    }

    public void onAddClutch() throws IOException {
        showSideForm("/org/example/simulatorgui/view/form/clutch_form.fxml", "clutch");
    }


    @FXML
    private void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/form/car_form.fxml"));
            Node carPane = loader.load();
            carController = loader.getController();
            carController.setParentController(this);
            carFormContainer.getChildren().add(carPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
