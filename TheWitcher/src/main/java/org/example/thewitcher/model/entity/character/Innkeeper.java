package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.items.Equipment;
import org.example.thewitcher.model.items.Food;
import java.util.ArrayList;
import java.util.List;

public class Innkeeper extends Character {
    private Equipment cargo;
    private List<String> tasks;

    public Innkeeper(int x, int y) {
        super(x, y, "Innkeeper");
        this.cargo = new Equipment(name);
        this.tasks = new ArrayList<>();
        tasks.add("Hunt a wolf");
        tasks.add("Hunt a ghul");
        supply();
    }

    private void supply() {
        cargo.addItem(new Food("Bread"));
        cargo.addItem(new Food("Beer"));
        cargo.addItem(new Food("Fish"));
    }

    public Equipment getCargo() { return cargo; }
    public List<String> getTasks() { return tasks; }
}
