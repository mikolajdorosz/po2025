package org.example.simulatorgui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.simulatorgui.config.RaceConfig;
import org.example.simulatorgui.controller.MainController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        openRaceSetupWindow("/org/example/simulatorgui/view/main-view.fxml", stage, new RaceConfig());
    }
    public static void openRaceSetupWindow(
            String path,
            Stage stage,
            RaceConfig config
    ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        fxmlLoader.setControllerFactory(param -> {
            MainController controller = new MainController(config);
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
