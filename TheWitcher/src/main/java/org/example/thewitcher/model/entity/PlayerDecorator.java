package org.example.thewitcher.model.entity;

import org.example.thewitcher.model.items.AppliedEquipment;
import org.example.thewitcher.model.items.Equipment;

public abstract class PlayerDecorator extends Player {
    protected Player player;

    public PlayerDecorator(Player player) {
        this.player = player;
    }

    @Override
    public int getArmor() { return player.getArmor(); }
    @Override
    public void setArmor(int armor) { player.setArmor(armor); }

    @Override
    public int getHealth() { return player.getHealth(); }
    @Override
    public void setHealth(int health) { player.setHealth(health); }

    @Override
    public int getCoins() { return player.getCoins(); }
    @Override
    public void setCoins(int coins) { player.setCoins(coins); }

    @Override
    public double getWeight() { return player.getWeight(); }
    @Override
    public void setWeight(double weight) { player.setWeight(weight); }

    @Override
    public int getBonus() { return player.getBonus(); }
    @Override
    public void setBonus(int bonus) { player.setBonus(bonus); }

    @Override
    public int getX() { return player.getX(); }
    @Override
    public void setX(int x) { player.setX(x); }

    @Override
    public int getY() { return player.getY(); }
    @Override
    public void setY(int y) { player.setY(y); }

    @Override
    public Equipment getEquipment() { return player.getEquipment(); }
    @Override
    public AppliedEquipment getAppliedEquipment() { return player.getAppliedEquipment(); }
}