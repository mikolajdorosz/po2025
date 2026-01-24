package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;

public class MapController {
    private final Game game;

    public MapController(Game game) { this.game = game; }

    public void handleInput(KeyCode code) {
        switch (code) {
            case W -> game.movePlayer(0, -1);
            case S -> game.movePlayer(0, 1);
            case A -> game.movePlayer(-1, 0);
            case D -> game.movePlayer(1, 0);
            case I -> game.setState(GameState.INVENTORY);
        }
    }
}
