package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.entity.Interactable;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.Equipment;
import org.example.thewitcher.model.items.Silver;
import org.example.thewitcher.model.items.Steel;

public class Blacksmith extends Character implements Interactable {
    private Equipment stock;

    public Blacksmith(int x, int y) {
        super(x, y, "Blacksmith");
        this.stock = new Equipment("Blacksmith");
        this.stock.addItem(new Steel("Long Steel Sword"));
        this.stock.addItem(new Silver("Master Silver Sword"));
    }

    public Equipment getStock() { return stock; }

    @Override
    public void interact(Game game) {
        game.setCurrentInteractable(this);
        game.setState(GameState.INTERACTION_BLACKSMITH);
    }
}
