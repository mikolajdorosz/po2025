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
        equipment.addItem(new Silver("Wolf Silver Sword"));
        equipment.addItem(new Armor("Wolf Armor"));
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

    public Equipment getEquipment() { return equipment; }
    public AppliedEquipment getAppliedEquipment() { return appliedEquipment; }

    public void equip() {}

    public Player equipItem(Item item) {
        switch (item.getType()) {
            case "silver": return new SilverPlayerDecorator(this, item);
            case "steel": return new SteelPlayerDecorator(this, item);
            case "distance": return new DistancePlayerDecorator(this, item);
            case "armor": return new ArmorPlayerDecorator(this, item);
            default: return this;
        }
    }

    public String use(Item item) {
        if (item.getType().equals("food")) {
            getEquipment().removeItem(item);
            setWeight(getWeight() - item.getWeight());
            setHealth(getHealth() + item.getBonus() > 100 ? 100 : getHealth() + item.getBonus());
            return "Used: " + item.getName() + " (Health: " + getHealth() + ")";
        } else if (item.getType().equals("elixir")) {
            // Logic for elixir if needed, for now just remove
            getEquipment().removeItem(item);
            return "Used: " + item.getName();
        }
        return "Cannot use " + item.getName();
    }

    public String drop(Item item) {
        getEquipment().removeItem(item);
        setWeight(getWeight() - item.getWeight());
        return "Dropped: " + item.getName();
    }

    public String inspect(Item item) {
        return item.inspect();
    }

    public void takeOff(Player p, IWearable item) {
        if (p.getAppliedEquipment().getItems().contains(item.getName().toLowerCase())) {
            switch (item.getType()) {
                case "silver":
                    p.getEquipment().addItem(p.getAppliedEquipment().getSilver());
                    p.getAppliedEquipment().removeFromList(item.getName().toLowerCase());
                    p.getAppliedEquipment().setSilver(null);
                    break;
                case "steel":
                    p.getEquipment().addItem(p.getAppliedEquipment().getSteel());
                    p.getAppliedEquipment().removeFromList(item.getName().toLowerCase());
                    p.getAppliedEquipment().setSteel(null);
                    break;
                case "distance":
                    p.getEquipment().addItem(p.getAppliedEquipment().getDistance());
                    p.getAppliedEquipment().removeFromList(item.getName().toLowerCase());
                    p.getAppliedEquipment().setDistance(null);
                    break;
                case "armor":
                    p.setArmor(0);
                    p.getEquipment().addItem(p.getAppliedEquipment().getArmor());
                    p.getAppliedEquipment().removeFromList(item.getName().toLowerCase());
                    p.getAppliedEquipment().setArmor(null);
                    break;
            }
        }
    }
}
