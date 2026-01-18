package org.example.thewitcher.app;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new GameApp().start(stage);
    }
}
