package org.example.thewitcher.model.entity.character;

import org.example.thewitcher.model.entity.Entity;

public abstract class Character extends Entity {
    protected int health;

    public Character(int x, int y, String name) {
        super(x, y, name);
    }

    public int getHealth() {  return health; }
    public void setHealth(int health) { this.health = health; }
}
