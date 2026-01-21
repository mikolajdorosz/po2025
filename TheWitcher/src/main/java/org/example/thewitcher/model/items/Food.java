package org.example.thewitcher.model.items;

public class Food extends Item {
    private int healthIncrease;

    public Food(String name) {
        super(name);
        this.type = "food";
        this.healthIncrease = 25 + randBonus.nextInt(25); // 25 to 49
        this.weight = Math.round(Math.abs(randWeight.nextDouble() - randWeight.nextDouble()) * 10.0) / 10.0;
        this.price = 25 + randPrice.nextInt(25);
    }

    @Override
    public int getBonus() { return healthIncrease; }
    @Override
    public void setBonus(int bonus) { this.healthIncrease = bonus; }

    @Override
    public String inspect() {
        return super.inspect() + "\n" + "Health increase: " + healthIncrease;
    }
}
