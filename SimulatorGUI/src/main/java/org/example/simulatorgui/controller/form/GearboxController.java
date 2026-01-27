package org.example.simulatorgui.controller.form;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.simulatorgui.model.components.GearboxType;
import org.example.simulatorgui.repo.CarComponentsRepository;
import org.example.simulatorgui.model.components.Clutch;
import org.example.simulatorgui.model.components.Gearbox;
import org.example.simulatorgui.model.util.Utils;

import java.io.IOException;

public class GearboxController {
    @FXML private TextField gearboxNameTextField;
    @FXML private TextField gearboxPriceTextField;
    @FXML private TextField gearboxWeightTextField;
    @FXML private TextField gearsTextField;
    @FXML private ToggleGroup gearboxTypeToggleGroup;
    @FXML private ToggleButton typeAutoToggleButton;
    @FXML private ToggleButton typeManualToggleButton;
    @FXML private ComboBox<Clutch> clutchComboBox;
    @FXML private HBox clutchComboBoxContainer;
    @FXML private Button confirmGearboxButton;


    private final CarComponentsRepository carComponentsRepository = CarComponentsRepository.getInstance();
    private ObjectProperty<GearboxType> gearboxTypeProperty = new SimpleObjectProperty<>();
    private NewCarController addCarController;
    private ComponentsController carComponentsController;

    public ComboBox<Clutch> getClutchComboBox() { return clutchComboBox; }
    public void setAddCarController(NewCarController addCarController) { this.addCarController = addCarController; }
    public void setCarComponentsController(ComponentsController carComponentsController) { this.carComponentsController = carComponentsController; }

    @FXML
    private void initialize() {
        clutchComboBox.setItems(carComponentsRepository.getClutches());
        clutchComboBox.getSelectionModel().selectFirst();

        setupTypeToggling();
        validateInput();
        setDefaultComboBoxValue();
    }
    private void setupTypeToggling() {
        gearboxTypeToggleGroup = new ToggleGroup();
        typeAutoToggleButton.setToggleGroup(gearboxTypeToggleGroup);
        typeManualToggleButton.setToggleGroup(gearboxTypeToggleGroup);
        gearboxTypeToggleGroup.selectedToggleProperty().addListener(
                (obs, oldToggle, newToggle) -> {
                    if (newToggle == null) {
                        oldToggle.setSelected(true);
                        return;
                    }
                    GearboxType type = GearboxType.valueOf(newToggle.getUserData().toString());
                    gearboxTypeProperty.set(type);
                    clutchComboBoxContainer.setDisable(type == GearboxType.AUTOMATIC);
                }
        );
        Toggle selected = gearboxTypeToggleGroup.getSelectedToggle();
        if (selected != null) {
            GearboxType type = GearboxType.valueOf(selected.getUserData().toString());
            gearboxTypeProperty.set(type);
            clutchComboBoxContainer.setDisable(type == GearboxType.AUTOMATIC);
        }
    }
    private void validateInput() {
        gearboxPriceTextField.setTextFormatter(Utils.createDecimalTextFormatter());
        gearboxWeightTextField.setTextFormatter(Utils.createDecimalTextFormatter());
        gearsTextField.setTextFormatter(Utils.createIntegerTextFormatter());
        confirmGearboxButton.disableProperty().bind(
            gearboxNameTextField.textProperty().isEmpty()
                .or(gearboxPriceTextField.textProperty().isEmpty())
                .or(gearboxWeightTextField.textProperty().isEmpty())
                .or(gearsTextField.textProperty().isEmpty())
                .or(clutchComboBox.valueProperty().isNull()
                    .and(gearboxTypeProperty.isEqualTo(GearboxType.MANUAL))
                )
        );
    }

    public Gearbox getGearboxFromInput() {
        Clutch clutch = clutchComboBox.getValue();
        carComponentsRepository.addClutchIfAbsent(clutch);
        clutchComboBox.getSelectionModel().select(clutch);

        String name = gearboxNameTextField.getText();
        if (carComponentsRepository.isDuplicateGearboxName(name)) {
            Utils.showDuplicateAlert("gearbox name", name);
            return null;
        }
        try {
            int gears = Integer.parseInt(gearsTextField.getText());
            double weight = Double.parseDouble(gearboxWeightTextField.getText());
            double price = Double.parseDouble(gearboxPriceTextField.getText());
            GearboxType type = gearboxTypeProperty.get();
            if (type == GearboxType.MANUAL) return new Gearbox(gears, type, name, weight, price, clutch);
            else return new Gearbox(gears, type, name, weight, price);
        } catch (NumberFormatException e) { throw new IllegalStateException("Input value is incorrect!"); }
    }

    // ===================== ACTIONS =====================
    @FXML private void onCreateClutch() throws IOException {
        ClutchController controller = addCarController.showForm(
                "clutch-form.fxml",
                addCarController.getClutchForm(),
                addCarController.getEngineGearboxForm()
        );
        controller.setAddCarController(addCarController);
        controller.setGearboxController(this);
    }
    @FXML private void onDeleteClutch() {
        Clutch selected = clutchComboBox.getValue();
        if (selected != null) carComponentsRepository.removeClutch(selected);
        if (!carComponentsRepository.getClutches().isEmpty()) clutchComboBox.getSelectionModel().selectFirst();
        setDefaultComboBoxValue();
    }
    @FXML private void onConfirm() {
        Gearbox gearbox = getGearboxFromInput();
        if (gearbox == null) return;
        carComponentsRepository.addGearboxIfAbsent(gearbox);
        carComponentsController.getGearboxComboBox().getSelectionModel().select(gearbox);
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
    @FXML private void onCancel() {
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
    private void setDefaultComboBoxValue() {
        clutchComboBox.setButtonCell(new ListCell<>() {     // default value for empty ComboBox
            @Override
            protected void updateItem(Clutch item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) setText("Select clutch");
                else setText(item.toString());
            }
        });
    }
}
