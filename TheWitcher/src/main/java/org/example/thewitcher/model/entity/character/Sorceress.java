package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.entity.Interactable;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;

public class Sorceress extends Character implements Interactable {
    private int serviceFee = 10;
    private java.util.Map<String, String> recipes;

    public Sorceress(int x, int y) {
        super(x, y, "Sorceress");

        this.recipes = new java.util.LinkedHashMap<>();
        this.recipes.put("Thunderbolt", "Wolfsbane");
        this.recipes.put("Resistance Potion", "Verbena");
    }

    public int getServiceFee() { return serviceFee; }
    public java.util.Map<String, String> getRecipes() { return recipes; }

    @Override
    public void interact(Game game) {
        game.setCurrentInteractable(this);
        game.setState(GameState.INTERACTION_SORCERESS);
    }
}
