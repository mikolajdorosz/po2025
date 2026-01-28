package org.example.simulatorgui.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/org/example/simulatorgui/view/main-view.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("CarSimulator - Setup");
        stage.setMaximized(true);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
