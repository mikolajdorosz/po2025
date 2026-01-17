package org.example.thewitcher.app;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.thewitcher.controller.GameController;
import org.example.thewitcher.view.GameView;

public class GameApp {
    public void start(Stage stage) {
        GameConfig config = GameConfig.defaultConfig();
        GameView view = new GameView(config.getWindowWidth(), config.getWindowHeight());
        GameController controller = new GameController(view);
        Scene scene = new Scene(new StackPane(view.getCanvas()));

        controller.attachInput(scene);
        stage.setTitle(config.getTitle());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        controller.start();
    }
}
