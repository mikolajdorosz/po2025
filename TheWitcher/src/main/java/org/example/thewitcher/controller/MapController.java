package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.entity.Entity;
import org.example.thewitcher.model.entity.Interactable;

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
            case E -> checkInteraction();
        }
    }

    private void checkInteraction() {
        int playerX = game.getPlayer().getX();
        int playerY = game.getPlayer().getY();

        for (Entity entity : game.getLocation().getEntities()) {
            if (entity instanceof Interactable interactable) {
                if (isAdjacent(playerX, playerY, entity.getX(), entity.getY())) {
                    interactable.interact(game);
                    return;
                }
            }
        }
    }

    private boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2) == 1;
    }
}
