package org.example.simulatorgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.simulatorgui.controller.MenuController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
        Parent root = fxmlLoader.load();    // loads controller

//        MenuController controller = fxmlLoader.getController();
//        controller.setStage(stage);
        stage.setTitle("CarSimulator");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
