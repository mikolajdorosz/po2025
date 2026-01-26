package org.example.simulatorgui.controller.form;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import simulator.*;

import java.io.IOException;

public class CarComponentsController {
    @FXML private ComboBox<Engine> engineComboBox;
    @FXML private ComboBox<Gearbox> gearboxComboBox;

    private final CarComponentsRepository carComponentsRepository = CarComponentsRepository.getInstance();
    private AddCarController addCarController;

    public ComboBox<Engine> getEngineComboBox() { return engineComboBox; }
    public ComboBox<Gearbox> getGearboxComboBox() { return gearboxComboBox; }
    public void setAddCarController(AddCarController addCarController) { this.addCarController = addCarController; }

    @FXML private void initialize() {
        engineComboBox.setItems(carComponentsRepository.getEngines());
        gearboxComboBox.setItems(carComponentsRepository.getGearboxes());
        setDefaultEngineComboBoxValue();
        setDefaultGearboxComboBoxValue();
    }

    public Engine getEngineFromInput() {
        Engine engine = engineComboBox.getValue();
        carComponentsRepository.addEngineIfAbsent(engine);
        return engine;
    }
    public Gearbox getGearboxFromInput() {
        Gearbox gearbox = gearboxComboBox.getValue();
        carComponentsRepository.addGearboxIfAbsent(gearbox);
        return gearbox;
    }

    // ===================== ACTIONS =====================
    @FXML private void onNewEngine() throws IOException {
        EngineController controller = addCarController.showForm(
                "engine-form.fxml",
                addCarController.getEngineGearboxForm(),
                addCarController.getCarComponentsForm(),
                addCarController.getCarBasicInfoForm()
        );
        controller.setAddCarController(addCarController);
        controller.setCarComponentsController(this);
    }
    @FXML private void onDeleteEngine() {
        Engine selected = engineComboBox.getValue();
        if (selected != null) carComponentsRepository.removeEngine(selected);
        setDefaultEngineComboBoxValue();
    }
    @FXML private void onNewGearbox() throws IOException {
        GearboxController controller = addCarController.showForm(
                "gearbox-form.fxml",
                addCarController.getEngineGearboxForm(),
                addCarController.getCarComponentsForm(),
                addCarController.getCarBasicInfoForm()
        );
        controller.setAddCarController(addCarController);
        controller.setCarComponentsController(this);
    }
    @FXML private void onDeleteGearbox() {
        Gearbox selected = gearboxComboBox.getValue();
        if (selected != null) carComponentsRepository.removeGearbox(selected);
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
