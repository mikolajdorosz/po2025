package org.example.thewitcher.model.entity;

import org.example.thewitcher.model.items.*;

public class Player {
    private int x;
    private int y;
    protected String name;
    protected int health;
    protected int coins;
    protected int armor;
    protected double weight;
    protected int bonus;

    protected Equipment equipment;
    protected AppliedEquipment appliedEquipment;

    public Player() {
        this.x = 3;
        this.y = 11;
        this.name = "Geralt";
        this.health = 100;
        this.coins = 100;
        this.armor = 0;
        this.weight = 0;

        this.equipment = new Equipment(name);
        this.appliedEquipment = new AppliedEquipment();

        initializeEquipment();
        updateWeight();
    }

    private void initializeEquipment() {
        equipment.addItem(new Food("Bread"));
        equipment.addItem(new Food("Cottage"));
    }

    private void updateWeight() {
        weight = 0;
        for (Item item : equipment.getItems()) {
            weight += item.getWeight();
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }

    public int getArmor() { return armor; }
    public void setArmor(int armor) { this.armor = armor; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getBonus() { return bonus; }
    public void setBonus(int bonus) { this.bonus = bonus; }
}
