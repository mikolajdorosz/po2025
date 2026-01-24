package org.example.thewitcher.controller;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.entity.character.Armorer;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.*;

import java.util.List;

public class ArmorerController {
    private final Game game;

    public ArmorerController(Game game) {
        this.game = game;
    }

    public void handleInput(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(GameState.MAP);
            game.setCurrentInteractable(null);
            game.setMessage("Closed trade.");
            return;
        }

        Armorer armorer = (Armorer) game.getCurrentInteractable();
        Player player = game.getPlayer();

        if (code.isLetterKey()) {
            char key = code.toString().toLowerCase().charAt(0);
            processAction(key, armorer, player);
        }
    }

    private void processAction(char key, Armorer armorer, Player player) {
        char currentKey = 'a';

        for (Item item : armorer.getStock().getItems()) {
            if (currentKey == key) {
                buyItem(player, item);
                return;
            }
            currentKey++;
        }

        AppliedEquipment applied = player.getAppliedEquipment();
        List<Item> appliedItems = java.util.Arrays.asList(
                applied.getSilver(), applied.getSteel(), applied.getDistance(), applied.getArmor()
        );

        for (Item item : appliedItems) {
            if (item instanceof Armor armor) {
                if (currentKey == key) { // Sell
                    sellItem(player, armor);
                    return;
                }
                currentKey++;
                if (currentKey == key) { // Repair
                    repairItem(player, armor);
                    return;
                }
                currentKey++;
            }
        }

        for (Item item : player.getEquipment().getItems()) {
            if (item instanceof Armor armor) {
                if (currentKey == key) { // Sell
                    sellItem(player, armor);
                    return;
                }
                currentKey++;
                if (currentKey == key) { // Repair
                    repairItem(player, armor);
                    return;
                }
                currentKey++;
            }
        }
    }

    private void buyItem(Player player, Item item) {
        if (player.getCoins() >= item.getPrice()) {
            player.setCoins(player.getCoins() - item.getPrice());
            player.getEquipment().addItem(item);
            game.setMessage("Bought " + item.getName());
        } else {
            game.setMessage("Not enough coins!");
        }
    }

    private void sellItem(Player player, Armor armor) {
        int price = calculateSellPrice(armor);
        player.setCoins(player.getCoins() + price);

        if (player.getEquipment().getItems().contains(armor)) {
            player.getEquipment().removeItem(armor);
        } else {
            player.getAppliedEquipment().removeFromList(armor.getName().toLowerCase());
            if (player.getAppliedEquipment().getArmor() == armor) player.getAppliedEquipment().setArmor(null);
        }
        game.setMessage("Sold " + armor.getName() + " for " + price);
    }

    private void repairItem(Player player, Armor armor) {
        int cost = calculateRepairCost(armor);
        if (cost == 0) {
            game.setMessage("Already perfect condition.");
            return;
        }
        if (player.getCoins() >= cost) {
            player.setCoins(player.getCoins() - cost);
            armor.setCondition(100);
            game.setMessage("Repaired " + armor.getName());
        } else {
            game.setMessage("Not enough coins to repair!");
        }
    }

    private int calculateSellPrice(Armor armor) {
        return (int) (armor.getPrice() * (armor.getCondition() / 100.0) * 0.5);
    }

    private int calculateRepairCost(Armor armor) {
        return (100 - armor.getCondition()) * 5;
    }
}
