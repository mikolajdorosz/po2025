package org.example.simulatorgui.controller.maincomponent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.simulatorgui.Main;
import org.example.simulatorgui.controller.MainController;

import java.io.IOException;

public class TopbarController {
    @FXML private Button exitButton;
    private Runnable onExit;

    public void setOnExit(Runnable onExit) { this.onExit = onExit; }

    @FXML public void onExit() throws IOException {
        if (onExit != null) onExit.run();
        closeWindow();
    }
    private void closeWindow() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
