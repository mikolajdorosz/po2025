package org.example.thewitcher.model.entity.monster;

import org.example.thewitcher.model.entity.Entity;

public class Monster extends Entity {
    protected int health, damage;
    protected boolean flying;

    public Monster(int x, int y, String name) {
        super(x, y, name);
        this.health = 100;
        this.flying = false;
    }

    public int getHealth() {  return health; }
    public void setHealth(int health) { this.health = health; }
    public boolean getIsFlying() { return flying; }
}
