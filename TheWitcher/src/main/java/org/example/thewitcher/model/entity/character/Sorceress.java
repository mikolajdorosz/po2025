package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.entity.Interactable;
import org.example.thewitcher.model.game.Game;
import org.example.thewitcher.model.game.GameState;
import java.util.ArrayList;
import java.util.List;

public class Sorceress extends Character implements Interactable {
    private List<String> tasks;
    private int targetNumber;

    public Sorceress(int x, int y) {
        super(x, y, "Sorceress");
        this.targetNumber = 5;
        this.tasks = new ArrayList<>();
        tasks.add("Gather " + targetNumber + " pieces of Verbena");
        tasks.add("Gather " + targetNumber + " pieces of Wolfsbane");
    }

    public List<String> getTasks() { return tasks; }

    @Override
    public void interact(Game game) {
        game.setCurrentInteractable(this);
        game.setState(GameState.INTERACTION_SORCERESS);
    }
}
