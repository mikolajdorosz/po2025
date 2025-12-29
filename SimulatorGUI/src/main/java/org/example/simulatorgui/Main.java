package org.example.simulatorgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/race-setup.fxml"));
        Parent root = fxmlLoader.load();    // loads controller

//        MenuController controller = fxmlLoader.getController();
//        controller.setStage(stage);
        stage.setTitle("CarSimulator");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/simulatorgui/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
