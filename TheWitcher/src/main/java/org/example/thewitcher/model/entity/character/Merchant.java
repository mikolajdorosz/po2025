package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.entity.Interactable;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import org.example.thewitcher.model.items.*;

public class Merchant extends Character implements Interactable {
    private Equipment cargo;
    private int serviceFee = 5;

    public Merchant(int x, int y) {
        super(x, y, "Merchant");
        this.cargo = new Equipment(name);
        supply();
    }

    private void supply() {
        cargo.addItem(new Food("Sweet"));
        cargo.addItem(new Food("Berries"));
        cargo.addItem(new Food("Chicken"));
        cargo.addItem(new Food("Pepper"));
        cargo.addItem(new Food("Lamb stew"));
        // Herbs and other items can be added here
    }

    public Equipment getCargo() {
        return cargo;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    @Override
    public void interact(Game game) {
        game.setCurrentInteractable(this);
        game.setState(GameState.INTERACTION_MERCHANT);
    }
}
