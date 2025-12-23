package org.example.simulatorgui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
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
    @FXML private VBox carFormContainer;
    @FXML private VBox engineFormContainer;
    @FXML private VBox gearboxFormContainer;
    @FXML private VBox clutchFormContainer;
    private MenuController menuController;
    private CarController carController;

    public VBox getCarFormContainer() {
        return carFormContainer;
    }
    public VBox getEngineFormContainer() {
        return engineFormContainer;
    }
    public VBox getGearboxFormContainer() {
        return gearboxFormContainer;
    }
    public VBox getClutchFormContainer() {
        return clutchFormContainer;
    }
    public void setMenuController(MenuController menuController) { this.menuController = menuController; }

    @FXML
    private void initialize() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== ACTIONS =====================
    public CarController showCarForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/form/car_form.fxml"));
        Node form = loader.load();

        carController = loader.getController();
        carController.setMenuController(menuController);   // NOW menuController is guaranteed to exist
        carController.setAddCarController(this);

        carFormContainer.getChildren().setAll(form);
        return carController;
    }
    public <T> T showForm(String view, VBox toShow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/form/" + view));
        Node form = loader.load();
        T controller = loader.getController();
        toShow.getChildren().setAll(form); // Placing form in add_car view
        return controller;
    }
    public <T> T showForm(String view, VBox toShow, VBox toDisable) throws IOException {
        T controller = showForm(view, toShow);
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
    public void closeWindow() {
        Stage stage = (Stage) carFormContainer.getScene().getWindow();
        stage.close();
    }
}
