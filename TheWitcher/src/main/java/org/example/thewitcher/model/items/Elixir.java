package org.example.thewitcher.model.items;

public class Elixir extends Item{

    public Elixir(String name) {
        super(name);
        this.type = "elixir";

        this.weight = Math.round(Math.abs(randWeight.nextDouble() - randWeight.nextDouble()) * 10.0) / 10.0;
        this.price = 100 + randPrice.nextInt(200); // 100 to 299
    }
}
