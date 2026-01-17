package org.example.thewitcher.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.GameView;

public class GameController {
    private final Game game;
    private final GameView view;

    public GameController(GameView view) {
        this.view = view;
        this.game = new Game();
    }

    public void start() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                view.render(game);
            }
        }.start();
    }
    public void attachInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> game.movePlayer(0, -1);
                case S -> game.movePlayer(0, 1);
                case A -> game.movePlayer(-1, 0);
                case D -> game.movePlayer(1, 0);
            }
        });
    }
}
