package org.example.thewitcher.model.items;

public abstract class Weapon extends Item implements IWearable{
    protected int damage;
    protected int condition;

    public Weapon(String name) {
        super(name);
        this.condition = 100;
        this.damage = 5 + randBonus.nextInt(5);
        this.price = 500 + randPrice.nextInt(1000); // 500 to 1499
        this.weight = (2 + randWeight.nextInt(2)) + Math.round(randWeight.nextDouble() * 10.0) / 10.0;
    }

    @Override
    public int getCondition() { return condition; }
    @Override
    public void setCondition(int condition) { this.condition = condition; }

    @Override
    public int getBonus() { return damage; }
    @Override
    public void setBonus(int bonus) { this.damage = bonus; }

    @Override
    public void inspect() {
        super.inspect();
        System.out.println("Damage: " + damage);
        System.out.println("Condition: " + condition);
    }
}
