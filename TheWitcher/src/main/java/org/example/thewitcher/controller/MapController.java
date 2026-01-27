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
            case ESCAPE -> game.setState(GameState.PAUSE_MENU);
        }
    }

    private void checkInteraction() {
        int playerX = game.getPlayer().getX();
        int playerY = game.getPlayer().getY();

        // Check for entity interaction
        for (Entity entity : game.getLocation().getEntities()) {
            if (entity instanceof Interactable interactable) {
                if (isAdjacent(playerX, playerY, entity.getX(), entity.getY())) {
                    interactable.interact(game);
                    return;
                }
            }
        }

        // Check for herb gathering (standing on the herb)
        org.example.thewitcher.model.map.point.Point point = game.getLocation().getPoint(playerX, playerY);
        if (point != null && point.getMarker() == org.example.thewitcher.model.map.data.ObjectDrawings.herb()) {
            org.example.thewitcher.model.items.Herb herb = org.example.thewitcher.model.items.Herb.random();
            game.getPlayer().getEquipment().addItem(herb);
            game.getLocation().removeHerb(playerX, playerY);
            game.setMessage("Gathered " + herb.getName());
        }
    }

    private boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2) == 1;
    }
}
