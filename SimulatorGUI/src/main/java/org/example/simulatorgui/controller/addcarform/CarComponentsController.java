package org.example.simulatorgui.controller.addcarform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<Engine> engines = FXCollections.observableArrayList();
    private ObservableList<Gearbox> gearboxes = FXCollections.observableArrayList();
    private AddCarController addCarController;

    public ComboBox<Engine> getEngineComboBox() { return engineComboBox; }
    public ComboBox<Gearbox> getGearboxComboBox() { return gearboxComboBox; }
    public ObservableList<Engine> getEngines() { return engines; }
    public ObservableList<Gearbox> getGearboxes() { return gearboxes; }

    public void setAddCarController(AddCarController addCarController) { this.addCarController = addCarController; }

    @FXML
    private void initialize() {
        engines = CarComponentsStorage.getEngines();
        gearboxes = CarComponentsStorage.getGearboxes();
        engineComboBox.setItems(engines);
        gearboxComboBox.setItems(gearboxes);
    }

    public Engine getEngineFromInput() {
        Engine engine = engineComboBox.getValue();
        if (!engines.contains(engine)) CarComponentsStorage.addEngine(engine);
        engineComboBox.getSelectionModel().select(engine);
        return engine;
    }
    public Gearbox getGearboxFromInput() {
        Gearbox gearbox = gearboxComboBox.getValue();
        if (!gearboxes.contains(gearbox)) CarComponentsStorage.addGearbox(gearbox);
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
        if (selected != null) engines.remove(selected);
        setDefaultEngineComboBoxValue();
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
        if (selected != null) gearboxes.remove(selected);
        setDefaultGearboxComboBoxValue();
    }
    private void setDefaultEngineComboBoxValue() {
        engineComboBox.setButtonCell(new ListCell<>() {     // default value for empty ComboBox
            @Override
            protected void updateItem(Engine item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select engine");
                else setText(item.toString());
            }
        });
    }
    private void setDefaultGearboxComboBoxValue() {
        gearboxComboBox.setButtonCell(new ListCell<>() {     // default value for empty ComboBox
            @Override
            protected void updateItem(Gearbox item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select gearbox");
                else setText(item.toString());
            }
        });
    }
}
