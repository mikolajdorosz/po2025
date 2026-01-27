package org.example.thewitcher.controller;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.service.GameStateManager;

public class PauseMenuController {
    private final Game game;
    private final Runnable onRestart;
    private final Runnable onStopTimer;

    public PauseMenuController(Game game, Runnable onRestart, Runnable onStopTimer) {
        this.game = game;
        this.onRestart = onRestart;
        this.onStopTimer = onStopTimer;
    }

    public void handleInput(KeyCode code) {
        switch (code) {
            case ESCAPE -> game.setState(GameState.MAP);
            case DIGIT1 -> { // Restart
                GameStateManager.deleteSave();
                onRestart.run();
                game.setState(GameState.MAP);
            }
            case DIGIT2 -> { // Save & Quit
                GameStateManager.saveGame(game);
                onStopTimer.run();
                Platform.exit();
            }
        }
    }
}
