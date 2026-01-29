package org.example.simulatorgui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.car.CarController;
import org.example.simulatorgui.controller.car.CarHUDController;
import org.example.simulatorgui.controller.car.CarTile;
import org.example.simulatorgui.model.car.ICarListener;
import org.example.simulatorgui.model.util.Utils;
import org.example.simulatorgui.repo.CarRepository;
import org.example.simulatorgui.controller.form.NewCarController;
import org.example.simulatorgui.controller.form.ComponentsController;
import org.example.simulatorgui.model.car.Car;
import org.example.simulatorgui.model.util.Position;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController implements ICarListener {
    @FXML private ComboBox<Car> storedCarsComboBox;
    @FXML private Pane raceTrackPane;
    @FXML private VBox carContainer;
    @FXML private VBox hudContainer;
    @FXML private Button startButton;
    @FXML private Button targetButton;

    private final CarRepository carRepository;
    private CarHUDController carHUDController;
    private final Map<Car, CarTile> carTiles = new HashMap<>();
    private final Map<Car, ImageView> carViews = new HashMap<>();
    private final Map<Car, ImageView> flagViews = new HashMap<>();
    private Car car;

    public MainController() throws InterruptedException { this.carRepository = new CarRepository(); }

    private void setCar(Car car) {
        if (this.car != null) this.car.removeListener(this);
        this.car = car;
        if (this.car != null) car.addListener(this);
    }

    // ===================== INITIALIZATION =====================
    @FXML private void initialize() throws IOException {
        storedCarsComboBox.setItems(carRepository.getStoredCars());
        storedCarsComboBox.getSelectionModel().selectFirst();
        setCar(storedCarsComboBox.getSelectionModel().getSelectedItem());
        setDefaultComboBoxValue();

        carHUDController = loadCarHUD();
        carHUDController.setCar(car);

        trackInteraction();
        handleCarSelection();
    }
    private void setDefaultComboBoxValue() {
        storedCarsComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Car item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? "Select car" : item.toString());
            }
        });
    }

    // ===================== SIMULATION ACTIONS =====================
    private void handleCarSelection() {
        storedCarsComboBox.setOnAction(e -> {
            setCar(storedCarsComboBox.getSelectionModel().getSelectedItem());
            carHUDController.setCar(car);
            carHUDController.setHasTarget(hasTargetPosition());
            toggleButtons();
        });
    }
    private void trackInteraction() {
        raceTrackPane.setOnMouseClicked(e -> {
            if (car == null) return;
            if (car.getToggler()) {
                try {
                    placeCar(e.getX(), e.getY());
                } catch (IOException ex) { throw new RuntimeException(ex); }
            } else placeTarget(e.getX(), e.getY());
        });
    }
    private void placeCar(double x, double y) throws IOException {
        if (hasStartingPosition()) return;
        Position position = new Position(x, y);
        int index = storedCarsComboBox.getSelectionModel().getSelectedIndex();
        car.setStartingPosition(position);
        ImageView carImageView = placeImg("cars/car"+ (index+1)%6 +".png", position);
        car.setImage(carImageView.getImage());
        carViews.put(car, carImageView);
        car.setToggler(!car.getToggler());
        carRepository.getCarsOnTrack().add(car);
        CarTile tile = loadCarTile(car);
        carTiles.put(car, tile);
        carContainer.getChildren().add(tile.node());
        startButton.getStyleClass().setAll("btn", "btn-grey");
        targetButton.getStyleClass().setAll("btn", "btn-red");
    }
    private void placeTarget(double x, double y) {
        if (hasTargetPosition()) return;
        Position position = new Position(x, y);
        int index = storedCarsComboBox.getSelectionModel().getSelectedIndex();
        car.setCurrentTarget(position);
        flagViews.put(car, placeImg("flags/flag"+ (index+1)%6 +".png", position));
        car.setToggler(!car.getToggler());
        targetButton.getStyleClass().setAll("btn", "btn-grey");
    }
    private ImageView placeImg(String img, Position pos) {
        ImageView imageView = new ImageView(new Image(
                getClass().getResource("/org/example/simulatorgui/images/" + img).toExternalForm()
        ));
        imageView.setFitWidth(50);
        imageView.setFitHeight(40);
        imageView.setLayoutX(pos.getX());
        imageView.setLayoutY(pos.getY());
        raceTrackPane.getChildren().add(imageView);
        return imageView;
    }
    @FXML private void onClearTrack() {
        car.setToggler(true);
        car.setStartingPosition(null);
        car.setCurrentTarget(null);
        clearCarImage();
        clearFlagImage();
        clearCarTile();
        carRepository.getCarsOnTrack().remove(car);
        toggleButtons();
    }
    private void refresh() {
        for (Car car : carRepository.getStoredCars()) {
            ImageView view = carViews.get(car);
            if (view == null || car.getCurrentPosition() == null) continue;
            view.setLayoutX(car.getCurrentPosition().getX());
            view.setLayoutY(car.getCurrentPosition().getY());
            if (car.getCurrentTarget() == null && car.getCurrentPosition() != null) clearFlagImage();
        }
    }
    private void toggleButtons() {
        startButton.getStyleClass().setAll("btn", "btn-grey");
        targetButton.getStyleClass().setAll("btn", "btn-grey");
        if (car == null) return;
        if (car.getToggler() && !hasStartingPosition()) startButton.getStyleClass().setAll("btn", "btn-blue");
        else if (!car.getToggler() && !hasTargetPosition()) targetButton.getStyleClass().setAll("btn", "btn-red");
    }
    private boolean hasStartingPosition() { return car.getStartingPosition() != null; }
    private boolean hasTargetPosition() { if (car != null) return car.getCurrentTarget() != null; return false; }
    private void clearFlagImage() {
        ImageView view = flagViews.remove(car);
        if (view != null) raceTrackPane.getChildren().remove(view);
    }
    private void clearCarImage() {
        ImageView view = carViews.remove(car);
        if (view != null) raceTrackPane.getChildren().remove(view);
    }
    private void clearCarTile() {
        CarTile tile = carTiles.remove(car);
        if (tile != null) carContainer.getChildren().remove(tile.node());
    }

    // ===================== CAR VIEW COMPONENTS =====================
    private CarTile loadCarTile(Car car) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/car/car.fxml")
        );
        Node tile = loader.load();
        CarController controller = loader.getController();
        controller.setCar(car);
        return new CarTile(controller, tile);
    }
    private CarHUDController loadCarHUD() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/simulatorgui/view/car/car-hud.fxml")
        );
        Node hud = loader.load();
        hudContainer.getChildren().setAll(hud);
        return loader.getController();
    }

    // ===================== FORM ACTIONS =====================
    @FXML private void onNewCar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/simulatorgui/view/form/new-car-view.fxml"));
        Parent root = loader.load();
        NewCarController newCarController = loader.getController();
        newCarController.setCarRepository(carRepository);
        ComponentsController componentsController = newCarController.showForm("components-form.fxml", newCarController.getCarComponentsForm());
        componentsController.setAddCarController(newCarController);
        newCarController.setCarComponentsController(componentsController);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("CarSimulator - Builder");
        stage.setMinWidth(1200);
        stage.setMinHeight(600);
        stage.show();
    }
    @FXML private void onDeleteCar() {
        boolean deleteCar = Utils.showDeleteAlert(car);
        if (!deleteCar) return;
        setDefaultComboBoxValue();
        clearFlagImage();
        clearCarImage();
        carRepository.getCarsOnTrack().remove(car);
        clearCarTile();
        Car selected = storedCarsComboBox.getValue();
        if (selected != null) {
            carRepository.getStoredCars().remove(selected);
            if (!carRepository.getStoredCars().isEmpty()) storedCarsComboBox.getSelectionModel().selectFirst();
        }
    }

    @Override public void onCarUpdated(Car car) { Platform.runLater(this::refresh); }
}
