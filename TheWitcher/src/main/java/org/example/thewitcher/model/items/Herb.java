package org.example.thewitcher.model.items;

public class Herb extends Item {
    private static final String[] HERB_TYPES = {"Verbena", "Wolfsbane", "Celandine"};

    public Herb(String name) {
        super(name);
        this.type = "herb";
        this.weight = Math.round(Math.abs(randWeight.nextDouble() - randWeight.nextDouble()) * 10.0) / 10.0;
        this.price = 5 + randPrice.nextInt(10);
    }

    public static Herb random() {
        String name = HERB_TYPES[java.util.concurrent.ThreadLocalRandom.current().nextInt(HERB_TYPES.length)];
        return new Herb(name);
    }
}
