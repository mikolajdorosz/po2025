package org.example.thewitcher.model.entity.character;

public class Bandit extends Character {
    private int damage;
    public Bandit(int x, int y) {
        super(x, y, "bandit");
        this.damage = 13;
        this.setHealth(200);
    }

    public int attack() { return damage; }
}
