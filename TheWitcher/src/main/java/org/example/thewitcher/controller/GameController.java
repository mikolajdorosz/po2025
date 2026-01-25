package org.example.thewitcher.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.example.thewitcher.controller.character.ArmorerController;
import org.example.thewitcher.controller.character.BlacksmithController;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.view.GameView;

public class GameController {
    private final Game game;
    private final GameView gameView;
    private final MapController mapController;
    private final InventoryController inventoryController;
    private final ArmorerController armorerController;
    private final BlacksmithController blacksmithController;
    private final BattleController battleController;
    private AnimationTimer timer;

    public GameController(GameView view) {
        this.game = new Game();
        this.gameView = view;
        this.mapController = new MapController(game);
        this.inventoryController = new InventoryController(game);
        this.armorerController = new ArmorerController(game);
        this.blacksmithController = new BlacksmithController(game);
        this.battleController = new BattleController(game);
    }

    public void start() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameView.render(game);
                if (game.getState() == GameState.GAME_OVER) { stop(); Platform.exit(); }
            }
        };
        timer.start();
    }
    public void attachInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (game.getState()) {
                case MAP -> mapController.handleInput(e.getCode());
                case INVENTORY,
                     INVENTORY_ITEM_ACTION_MENU,
                     INVENTORY_INSPECT_ITEM -> inventoryController.handleInput(e.getCode(), e.getText());
                case INTERACTION_ARMORER -> armorerController.handleInput(e.getCode());
                case INTERACTION_BLACKSMITH -> blacksmithController.handleInput(e.getCode());
                case INTERACTION_INNKEEPER,
                     INTERACTION_MERCHANT, INTERACTION_SORCERESS -> {
                    if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                        game.setState(org.example.thewitcher.model.game.GameState.MAP);
                        game.setCurrentInteractable(null);
                    }
                }
                case BATTLE -> battleController.handleInput(e.getCode(), e.getText());
                case GAME_OVER -> { timer.stop(); Platform.exit(); }
            }
        });
    }
}
