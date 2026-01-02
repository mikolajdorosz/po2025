package org.example.simulatorgui.controller.addcarform;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.simulatorgui.controller.AddCarController;
import simulator.*;

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
    private ObservableList<Clutch> clutches = FXCollections.observableArrayList();
    private StringProperty gearboxTypeProperty = new SimpleStringProperty();
    private AddCarController addCarController;
    private CarComponentsController carComponentsController;
    private String gearboxType;

    public ComboBox<Clutch> getClutchComboBox() { return clutchComboBox; }
    public ObservableList<Clutch> getClutches() { return clutches; }
    public void setAddCarController(AddCarController addCarController) { this.addCarController = addCarController; }
    public void setCarComponentsController(CarComponentsController carComponentsController) { this.carComponentsController = carComponentsController; }

    @FXML
    private void initialize() {
        clutches = FXCollections.observableArrayList(
                CarComponentsStorage.getClutches().stream()
                        .filter(c -> c != null)
                        .toList()
        );
        clutchComboBox.setItems(clutches.filtered(c -> c != null));

        setupTypeToggling();
        validateInput();
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
                    gearboxType = newToggle.getUserData().toString();
                    gearboxTypeProperty.set(gearboxType);
                    clutchComboBoxContainer.setDisable("automatic".equals(gearboxType));
                }
        );
        Toggle selected = gearboxTypeToggleGroup.getSelectedToggle();
        if (selected != null) {
            gearboxType = selected.getUserData().toString();
            gearboxTypeProperty.set(gearboxType);
            clutchComboBoxContainer.setDisable("automatic".equals(gearboxType));
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
                    .and(gearboxTypeProperty.isEqualTo("manual"))
                )
        );
    }

    public Gearbox getGearboxFromInput() {
        Clutch clutch = clutchComboBox.getValue();
        if (!clutches.contains(clutch)) CarComponentsStorage.addClutch(clutch);
        clutchComboBox.getSelectionModel().select(clutch);

        String name = gearboxNameTextField.getText();
        boolean duplicate = carComponentsController.getGearboxes().stream().anyMatch(c -> c.getName().equalsIgnoreCase(name));
        if (duplicate) {
            Utils.showDuplicateAlert("gearbox name", name);
            return null;
        }
        double weight, price;
        int gears;
        try {
            gears = Integer.parseInt(gearsTextField.getText());
            weight = Double.parseDouble(gearboxWeightTextField.getText());
            price = Double.parseDouble(gearboxPriceTextField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Input value is incorrect!");
        }
        if ("manual".equals(gearboxType)) {
            return new Gearbox(gears, gearboxType, name, weight, price, clutch);
        } else {
            return new Gearbox(gears, gearboxType, name, weight, price);
        }
    }
    // ===================== ACTIONS =====================
    @FXML
    private void onCreateClutch() throws IOException {
        ClutchController controller = addCarController.showForm(
                "clutch-form.fxml",
                addCarController.getClutchForm(),
                addCarController.getEngineGearboxForm()
        );
        controller.setAddCarController(addCarController);
        controller.setGearboxController(this);
    }
    @FXML
    private void onDeleteClutch() {
        Clutch selected = clutchComboBox.getValue();
        if (selected != null) clutches.remove(selected);
        setDefaultComboBoxValue();
    }
    @FXML
    private void onConfirm() {
        Gearbox gearbox = getGearboxFromInput();
        if (gearbox == null) return;
        carComponentsController.getGearboxes().add(gearbox);
        carComponentsController.getGearboxComboBox().getSelectionModel().select(gearbox);
        addCarController.closeForm(addCarController.getEngineGearboxForm(), addCarController.getCarComponentsForm(), addCarController.getCarBasicInfoForm());
    }
    @FXML
    private void onCancel() {
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
