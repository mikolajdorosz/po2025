package org.example.thewitcher.model.entity.character;

public class Ally extends Character {
    public Ally(int x, int y) {
        super(x, y, "Witcher Ally");
        this.health = 70;
    }

    // Add attack logic if needed
    public int attack() {
        return 15;
    }
}
