package org.example.thewitcher.model.entity.character;

public class Bandit extends Character {
    private int damage;
    public Bandit(int x, int y) {
        super(x, y, "bandit");
        this.damage = 8;
        this.setHealth(100);
    }

    public int attack() { return damage; }
}
