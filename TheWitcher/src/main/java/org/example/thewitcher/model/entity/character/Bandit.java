package org.example.thewitcher.model.entity.character;

public class Bandit extends Character {
    private int damage;
    public Bandit(int x, int y) {
        super(x, y, "bandit");
        this.damage = 6;
    }

    public int attack() { return damage; }
}
