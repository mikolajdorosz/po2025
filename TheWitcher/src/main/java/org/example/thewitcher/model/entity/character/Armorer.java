package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.entity.Interactable;
import org.example.thewitcher.model.entity.player.Player;
import org.example.thewitcher.model.items.Armor;
import org.example.thewitcher.model.items.Equipment;

public class Armorer extends Character implements Interactable {
    private Equipment stock;

    public Armorer(int x, int y) {
        super(x, y, "Armorer");
        this.stock = new Equipment("Armorer");
        // Add exemplary stock
        this.stock.addItem(new Armor("Kaer Morhen Armor"));
        this.stock.addItem(new Armor("Nilfgaardian Guard Armor"));
    }

    public Equipment getStock() { return stock; }

    @Override
    public void interact(Game game) {
        game.setCurrentInteractable(this);
        game.setState(GameState.INTERACTION_ARMORER);
    }
}
