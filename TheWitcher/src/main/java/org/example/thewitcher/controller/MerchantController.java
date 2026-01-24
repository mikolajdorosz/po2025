package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.entity.character.Merchant;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.Item;

public class MerchantController {
    private final Game game;

    public MerchantController(Game game) {
        this.game = game;
    }

    public void handleInput(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(GameState.MAP);
            game.setCurrentInteractable(null);
            game.setMessage("Closed trade.");
            return;
        }

        Merchant merchant = (Merchant) game.getCurrentInteractable();
        Player player = game.getPlayer();

        if (code.isLetterKey()) {
            char key = code.toString().toLowerCase().charAt(0);
            processAction(key, merchant, player);
        }
    }

    private void processAction(char key, Merchant merchant, Player player) {
        char currentKey = 'a';

        // Buy items
        for (Item item : merchant.getCargo().getItems()) {
            if (currentKey == key) {
                buyItem(player, item, merchant.getServiceFee());
                return;
            }
            currentKey++;
        }

        // Sell Food items
        for (Item item : player.getEquipment().getItems()) {
            if (item instanceof org.example.thewitcher.model.items.Food) {
                if (currentKey == key) {
                    sellItem(player, item);
                    return;
                }
                currentKey++;
            }
        }
    }

    private void sellItem(Player player, Item item) {
        player.setCoins(player.getCoins() + item.getPrice());
        player.getEquipment().removeItem(item);
        game.setMessage("Sold " + item.getName() + " for " + item.getPrice());
    }

    private void buyItem(Player player, Item item, int serviceFee) {
        int totalCost = item.getPrice() + serviceFee;
        if (player.getCoins() >= totalCost) {
            player.setCoins(player.getCoins() - totalCost);
            player.getEquipment().addItem(item);
            game.setMessage("Bought " + item.getName());
        } else {
            game.setMessage("Not enough coins!");
        }
    }
}
