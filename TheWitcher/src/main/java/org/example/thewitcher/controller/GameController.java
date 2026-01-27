package org.example.thewitcher.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import org.example.thewitcher.controller.character.ArmorerController;
import org.example.thewitcher.controller.character.BlacksmithController;
import org.example.thewitcher.controller.character.MerchantController;
import org.example.thewitcher.controller.character.SorceressController;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.view.GameView;
import org.example.thewitcher.service.GameStateManager;

public class GameController {
    private Game game;
    private final GameView gameView;
    private MapController mapController;
    private InventoryController inventoryController;
    private ArmorerController armorerController;
    private BlacksmithController blacksmithController;
    private MerchantController merchantController;
    private SorceressController sorceressController;
    private PauseMenuController pauseMenuController;
    private BattleController battleController;
    private AnimationTimer timer;



    public GameController(GameView view) {
        Game loadedGame = GameStateManager.loadGame();
        if (loadedGame != null) {
            this.game = loadedGame;
        } else {
            this.game = new Game();
        }

        this.gameView = view;
        initControllers();
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
                case PAUSE_MENU -> pauseMenuController.handleInput(e.getCode());
                case INVENTORY,
                     INVENTORY_ITEM_ACTION_MENU,
                     INVENTORY_INSPECT_ITEM -> inventoryController.handleInput(e.getCode(), e.getText());
                case INTERACTION_ARMORER -> armorerController.handleInput(e.getCode());
                case INTERACTION_BLACKSMITH -> blacksmithController.handleInput(e.getCode());
                case INTERACTION_MERCHANT -> merchantController.handleInput(e.getCode());
                case INTERACTION_SORCERESS -> sorceressController.handleInput(e.getCode());
                case INTERACTION_INNKEEPER -> {
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

    private void restartGame() {
        this.game = new Game();
        initControllers();
        game.setState(GameState.MAP);
    }

    private void initControllers() {
        this.mapController = new MapController(game);
        this.inventoryController = new InventoryController(game);
        this.armorerController = new ArmorerController(game);
        this.blacksmithController = new BlacksmithController(game);
        this.merchantController =  new MerchantController(game);
        this.sorceressController = new SorceressController(game);
        this.battleController = new BattleController(game);
        this.pauseMenuController = new PauseMenuController(game, this::restartGame, () -> timer.stop());
    }
}
