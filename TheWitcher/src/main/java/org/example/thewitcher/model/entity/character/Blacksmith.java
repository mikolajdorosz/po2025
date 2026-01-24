package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.entity.Interactable;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;

public class Blacksmith extends Character implements Interactable {
    public Blacksmith(int x, int y) {
        super(x, y, "Blacksmith");
    }

    @Override
    public void interact(Game game) {
        game.setCurrentInteractable(this);
        game.setState(GameState.INTERACTION_BLACKSMITH);
    }
}
