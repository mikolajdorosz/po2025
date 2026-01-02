package org.example.simulatorgui.controller.maincomponent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.simulatorgui.Main;
import org.example.simulatorgui.controller.MainController;

import java.io.IOException;

public class TopbarController {
    @FXML
    private Button exitButton;
    private MainController mainController;

    public void setMainController(MainController mainController) { this.mainController = mainController; }

    public void onExit() throws IOException {
        mainController.stopRace();
        closeWindow();
        Main.openRaceSetupWindow("/org/example/simulatorgui/view/race-setup.fxml", new Stage(), mainController.getRaceSetupController().getRaceCars(), mainController.getRaceSetupController().getStoredCars(), mainController.getRaceSetupController().getStartPosition(), mainController.getRaceSetupController().getCheckpointPositions(), mainController.getRaceSetupController().getFinishPosition());
    }
    private void closeWindow() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
