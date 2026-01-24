package org.example.thewitcher.model.entity.character;

import java.util.ArrayList;
import java.util.List;

public class Sorceress extends Character {
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
}
