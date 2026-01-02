package org.example.simulatorgui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.RaceSetupController;
import simulator.Car;
import simulator.Position;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        openRaceSetupWindow("view/race-setup.fxml", stage, null, null, null, null, null);
    }
    public static void openRaceSetupWindow(String path, Stage stage, ObservableList<Car> raceCars, ObservableList<Car> storedCars, Position start, ArrayList<Position> checkpoints, Position finish) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        if (raceCars != null && start != null && checkpoints != null && finish != null) {
            fxmlLoader.setControllerFactory(param -> {
                RaceSetupController controller = new RaceSetupController();
                controller.setRaceCars(raceCars);
                controller.setStoredCars(storedCars);
                controller.setStartPosition(start);
                controller.setCheckpointPositions(checkpoints);
                controller.setFinishPosition(finish);
                return controller;
            });
        }
        Parent root = fxmlLoader.load();
        stage.setTitle("CarSimulator - Setup");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
