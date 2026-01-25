package org.example.simulatorgui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.simulatorgui.config.RaceConfig;
import org.example.simulatorgui.controller.CarRepository;
import org.example.simulatorgui.controller.RaceSetupController;
import simulator.Car;
import simulator.Position;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        openRaceSetupWindow("view/race-setup.fxml", stage, new RaceConfig());
    }
    public static void openRaceSetupWindow(
            String path,
            Stage stage,
            RaceConfig config
    ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        fxmlLoader.setControllerFactory(param -> {
            RaceSetupController controller = new RaceSetupController(config);
            controller.setCarRepository(new CarRepository(config.getStoredCars(), config.getRaceCars()));
            return controller;
        });
        Parent root = fxmlLoader.load();
        stage.setTitle("CarSimulator - Setup");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
