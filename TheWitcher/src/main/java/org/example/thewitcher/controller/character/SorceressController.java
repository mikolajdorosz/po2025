package org.example.thewitcher.controller.character;

import javafx.scene.input.KeyCode;
import org.example.thewitcher.model.entity.character.Sorceress;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.Elixir;
import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.entity.player.Player;

import java.util.Optional;

public class SorceressController {
    private final Game game;

    public SorceressController(Game game) {
        this.game = game;
    }

    public void handleInput(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            game.setState(GameState.MAP);
            game.setCurrentInteractable(null);
            game.setMessage("Closed session.");
            return;
        }

        Sorceress sorceress = (Sorceress) game.getCurrentInteractable();
        Player player = game.getPlayer();

        if (code.isLetterKey()) {
            char key = code.toString().toLowerCase().charAt(0);
            processAction(key, sorceress, player);
        }
    }

    private void processAction(char key, Sorceress sorceress, Player player) {
        char currentKey = 'a';

        for (java.util.Map.Entry<String, String> entry : sorceress.getRecipes().entrySet()) {
            if (currentKey == key) {
                String elixirName = entry.getKey();
                String herbName = entry.getValue();
                craftElixir(player, sorceress, elixirName, herbName);
                return;
            }
            currentKey++;
        }
    }

    private void craftElixir(Player player, Sorceress sorceress, String elixirName, String herbName) {
        if (player.getCoins() < sorceress.getServiceFee()) {
            game.setMessage("Not enough coins!");
            return;
        }

        Optional<Item> herbOpt = player.getEquipment().getItems().stream()
                .filter(i -> i.getName().equalsIgnoreCase(herbName))
                .findFirst();

        if (herbOpt.isPresent()) {
            player.getEquipment().removeItem(herbOpt.get());
            player.setCoins(player.getCoins() - sorceress.getServiceFee());
            player.getEquipment().addItem(new Elixir(elixirName));
            game.setMessage("Crafted " + elixirName + "!");
        } else {
            game.setMessage("Missing ingredient: " + herbName);
        }
    }
}
