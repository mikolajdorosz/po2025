package org.example.thewitcher.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.view.GameView;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.items.IWearable;
import javafx.scene.input.KeyCode;

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
            switch (game.getState()) {
                case MAP -> {
                    switch (e.getCode()) {
                        case W -> game.movePlayer(0, -1);
                        case S -> game.movePlayer(0, 1);
                        case A -> game.movePlayer(-1, 0);
                        case D -> game.movePlayer(1, 0);
                        case I -> game.setState(org.example.thewitcher.model.game.GameState.INVENTORY);
                    }
                }
                case INVENTORY -> {
                    if (e.getCode() == javafx.scene.input.KeyCode.I || e.getCode() == javafx.scene.input.KeyCode.DIGIT0) {
                        game.setState(org.example.thewitcher.model.game.GameState.MAP);
                        game.setMessage("");
                    } else {
                        String charInput = e.getText();
                        if (charInput.length() == 1) {
                            char key = charInput.toLowerCase().charAt(0);
                            for (org.example.thewitcher.model.game.Game.InventoryEntry entry : game.getInventoryEntries()) {
                                if (entry.key == key) {
                                    game.setSelectedItem(entry.item);
                                    game.setState(org.example.thewitcher.model.game.GameState.ITEM_ACTION_MENU);
                                    game.setMessage("");
                                    break;
                                }
                            }
                        }
                    }
                }
                case ITEM_ACTION_MENU -> {
                    org.example.thewitcher.model.items.Item item = game.getSelectedItem();
                    if (item == null) {
                        game.setState(org.example.thewitcher.model.game.GameState.INVENTORY);
                        return;
                    }

                    switch (e.getCode()) {
                        case DIGIT0 -> game.setState(org.example.thewitcher.model.game.GameState.INVENTORY);
                        case DIGIT1 -> {
                            game.setMessage(game.getPlayer().inspect(item));
                            game.setState(org.example.thewitcher.model.game.GameState.INSPECT_ITEM);
                        }
                        case DIGIT2 -> {
                            // Check if item is currently equipped (in applied)
                            boolean isApplied = false;
                            for (org.example.thewitcher.model.game.Game.InventoryEntry entry : game.getInventoryEntries()) {
                                if (entry.item == item && entry.isApplied) {
                                    isApplied = true;
                                    break;
                                }
                            }

                            if (isApplied) {
                                // Unequip / Take off
                                // Using the existing takeOff method logic via player
                                if (item instanceof org.example.thewitcher.model.items.IWearable) {
                                    game.getPlayer().takeOff(game.getPlayer(), (org.example.thewitcher.model.items.IWearable) item);
                                    game.setMessage("Took off: " + item.getName());
                                } else {
                                    game.setMessage("Cannot take off " + item.getName());
                                }
                                game.setState(org.example.thewitcher.model.game.GameState.INVENTORY);
                            } else {
                                // Equip / Use
                                if (item.getType().equals("food") || item.getType().equals("elixir")) {
                                    String msg = game.getPlayer().use(item);
                                    game.setMessage(msg);
                                } else {
                                    org.example.thewitcher.model.entity.Player newPlayer = game.getPlayer().equipItem(item);
                                    game.setPlayer(newPlayer);
                                    game.setMessage("Equipped: " + item.getName());
                                }
                                game.setState(org.example.thewitcher.model.game.GameState.INVENTORY);
                            }
                        }
                        case DIGIT3 -> {
                            try {
                                game.getPlayer().takeOff(game.getPlayer(), (org.example.thewitcher.model.items.IWearable) item);
                            } catch (Exception ex) {
                                // Not wearable or not applied, ignore
                            }

                            String msg = game.getPlayer().drop(item);
                            game.setMessage(msg);
                            game.setState(org.example.thewitcher.model.game.GameState.INVENTORY);
                        }
                    }
                }
                case INSPECT_ITEM -> {
                    // Any key to close inspection
                    game.setState(org.example.thewitcher.model.game.GameState.ITEM_ACTION_MENU);
                    game.setMessage("");
                }
            }
        });
    }
}
