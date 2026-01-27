package org.example.thewitcher.model.items;

import java.util.Random;

public abstract class Item implements java.io.Serializable {
    protected String name;
    protected String type;
    protected int price;
    protected double weight;
    protected Random randWeight;
    protected Random randPrice;
    protected Random randBonus;

    public Item(String name) {
        this.name = name;
        this.randWeight = new Random();
        this.randPrice = new Random();
        this.randBonus = new Random();
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getCondition() { return 0; }
    public void setCondition(int condition) {}
    public int getBonus() { return 0; }
    public void setBonus(int bonus) {}

    public String inspect() {
        return "Name: " + name + "\n" +
                "Type: " + type + "\n" +
                "Weight: " + weight + "\n" +
                "Price: " + price;
    }
}
