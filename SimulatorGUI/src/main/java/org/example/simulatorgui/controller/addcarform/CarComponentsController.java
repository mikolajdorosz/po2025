package org.example.simulatorgui.controller.addcarform;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import org.example.simulatorgui.controller.AddCarController;
import org.example.simulatorgui.controller.RaceSetupController;
import simulator.*;

import java.io.IOException;

public class CarComponentsController {
    @FXML private ComboBox<Engine> engineComboBox;
    @FXML private ComboBox<Gearbox> gearboxComboBox;
    private AddCarController addCarController;

    public ComboBox<Engine> getEngineComboBox() {
        return engineComboBox;
    }
    public ComboBox<Gearbox> getGearboxComboBox() {
        return gearboxComboBox;
    }
    public void setAddCarController(AddCarController addCarController) {
        this.addCarController = addCarController;
    }

    public Engine getEngineFromInput() {
        Engine engine = engineComboBox.getValue();
        engineComboBox.getItems().add(engine);
        engineComboBox.getSelectionModel().select(engine);
        return engine;
    }
    public Gearbox getGearboxFromInput() {
        Gearbox gearbox = gearboxComboBox.getValue();
        gearboxComboBox.getItems().add(gearbox);
        gearboxComboBox.getSelectionModel().select(gearbox);
        return gearbox;
    }

    // ===================== ACTIONS =====================
    @FXML
    private void onNewEngine() throws IOException {
        EngineController controller = addCarController.showForm(
                "engine-form.fxml",
                addCarController.getEngineGearboxForm(),
                addCarController.getCarComponentsForm(),
                addCarController.getCarBasicInfoForm()
        );
        controller.setAddCarController(addCarController);
        controller.setCarComponentsController(this);
    }
    @FXML
    private void onDeleteEngine() {
        Engine selected = engineComboBox.getValue();
        if (selected != null) {
            engineComboBox.getItems().remove(selected);
        }
        engineComboBox.setButtonCell(new ListCell<Engine>() {     // default value for empty ComboBox
            @Override
            protected void updateItem(Engine item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select engine");
                else setText(item.toString());
            }
        });
    }
    @FXML
    private void onNewGearbox() throws IOException {
        GearboxController controller = addCarController.showForm(
                "gearbox-form.fxml",
                addCarController.getEngineGearboxForm(),
                addCarController.getCarComponentsForm(),
                addCarController.getCarBasicInfoForm()
        );
        controller.setAddCarController(addCarController);
        controller.setCarComponentsController(this);
    }
    @FXML
    private void onDeleteGearbox() {
        Gearbox selected = gearboxComboBox.getValue();
        if (selected != null) {
            gearboxComboBox.getItems().remove(selected);
        }
        gearboxComboBox.setButtonCell(new ListCell<Gearbox>() {     // default value for empty ComboBox
            @Override
            protected void updateItem(Gearbox item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select gearbox");
                else setText(item.toString());
            }
        });
    }
}
