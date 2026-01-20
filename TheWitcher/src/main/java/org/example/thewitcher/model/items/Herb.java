package org.example.thewitcher.model.items;

public class Herb extends Item {
    public Herb(String name) {
        super(name);
        this.type = "herb";
        this.weight = Math.round(Math.abs(randWeight.nextDouble() - randWeight.nextDouble()) * 10.0) / 10.0;
        this.price = 25 + randPrice.nextInt(50);
    }
}
