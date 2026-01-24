package org.example.thewitcher.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.GameView;

public class GameController {
    private final Game game;
    private final GameView gameView;
    private final MapController mapController;
    private final InventoryController inventoryController;
    private final ArmorerController armorerController;

    public GameController(GameView view) {
        this.game = new Game();
        this.gameView = view;
        this.mapController = new MapController(game);
        this.inventoryController = new InventoryController(game);
        this.armorerController = new ArmorerController(game);
    }

    public void start() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameView.render(game);
            }
        }.start();
    }
    public void attachInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (game.getState()) {
                case MAP -> mapController.handleInput(e.getCode());
                case INVENTORY,
                     INVENTORY_ITEM_ACTION_MENU,
                     INVENTORY_INSPECT_ITEM -> inventoryController.handleInput(e.getCode(), e.getText());
                case INTERACTION_ARMORER -> armorerController.handleInput(e.getCode());
                case INTERACTION_INNKEEPER, INTERACTION_BLACKSMITH,
                     INTERACTION_MERCHANT, INTERACTION_SORCERESS -> {
                    if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                        game.setState(org.example.thewitcher.model.game.GameState.MAP);
                        game.setCurrentInteractable(null);
                    }
                }
            }
        });
    }
}
