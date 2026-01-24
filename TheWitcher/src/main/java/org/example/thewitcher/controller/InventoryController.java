package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.IWearable;
import org.example.thewitcher.model.items.Item;

public class InventoryController {
    private final Game game;

    public InventoryController(Game game) { this.game = game; }

    public void handleInput(KeyCode code, String charInput) {
        switch (game.getState()) {
            case INVENTORY -> handleInventory(code, charInput);
            case INVENTORY_ITEM_ACTION_MENU -> handleInventoryItemActionMenu(code);
            case INVENTORY_INSPECT_ITEM -> handleInventoryInspectItem();
        }
    }

    private void handleInventory(KeyCode code, String charInput) {
        if (code == KeyCode.I || code == KeyCode.DIGIT0) {
            game.setState(GameState.MAP);
            game.setMessage("");
            return;
        }
        if (charInput.length() != 1) return;
        char key = charInput.toLowerCase().charAt(0);
        for (Game.InventoryEntry entry : game.getInventoryEntries()) {
            if (entry.key == key) {
                game.setSelectedItem(entry.item);
                game.setState(GameState.INVENTORY_ITEM_ACTION_MENU);
                game.setMessage("");
                break;
            }
        }
    }
    private void handleInventoryItemActionMenu(KeyCode code) {
        Item item = game.getSelectedItem();
        if (item == null) {
            game.setState(GameState.INVENTORY);
            return;
        }

        switch (code) {
            case DIGIT0 -> game.setState(GameState.INVENTORY);
            case DIGIT1 -> {
                game.setMessage(game.getPlayer().inspect(item));
                game.setState(GameState.INVENTORY_INSPECT_ITEM);
            }
            case DIGIT2 -> {
                // Check if item is currently equipped (in applied)
                boolean isApplied = false;
                for (Game.InventoryEntry entry : game.getInventoryEntries()) {
                    if (entry.item == item && entry.isApplied) {
                        isApplied = true;
                        break;
                    }
                }
                if (isApplied) {
                    // Unequip / Take off
                    // Using the existing takeOff method logic via player
                    if (item instanceof IWearable wearable) {
                        game.getPlayer().takeOff(game.getPlayer(), wearable);
                        game.setMessage("Took off: " + item.getName());
                    } else game.setMessage("Cannot take off " + item.getName());
                    game.setState(GameState.INVENTORY);
                } else {
                    // Equip / Use
                    if (item.getType().equals("food") || item.getType().equals("elixir")) {
                        game.setMessage(game.getPlayer().use(item));
                    } else {
                        game.setPlayer(game.getPlayer().equipItem(item));
                        game.setMessage("Equipped: " + item.getName());
                    }
                    game.setState(GameState.INVENTORY);
                }
            }
            case DIGIT3 -> {
                try {
                    game.getPlayer().takeOff(game.getPlayer(), (IWearable) item);
                } catch (Exception ex) {
                    // Not wearable or not applied, ignore
                }
                game.setMessage(game.getPlayer().drop(item));
                game.setState(GameState.INVENTORY);
            }
        }
    }
    private void handleInventoryInspectItem() {
        // Any key to close inspection
        game.setState(GameState.INVENTORY_ITEM_ACTION_MENU);
        game.setMessage("");
    }
}
