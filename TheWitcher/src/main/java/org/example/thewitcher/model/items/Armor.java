package org.example.thewitcher.model.items;

public class Armor extends Item implements IWearable {
    private int armorValue;
    private int condition;

    public Armor(String name) {
        super(name);
        this.type = "armor";
        this.condition = 100;
        this.armorValue = 25 + randBonus.nextInt(50); // 25 to 74
        this.price = 1000 + randPrice.nextInt(1000); // 1000 to 1999
        this.weight = (5 + randWeight.nextInt(2)) + Math.round(randWeight.nextDouble() * 10.0) / 10.0;
    }

    @Override
    public int getCondition() { return condition; }
    @Override
    public void setCondition(int condition) { this.condition = condition; }

    @Override
    public int getBonus() { return armorValue; }
    @Override
    public void setBonus(int bonus) { this.armorValue = bonus; }

    @Override
    public void inspect() {
        super.inspect();
        System.out.println("Armor: " + armorValue);
        System.out.println("Condition: " + condition);
    }

    public static Armor parse(Item item) {
        Armor armor = new Armor(item.getName());
        armor.setPrice(item.getPrice());
        armor.setType(item.getType());
        armor.setWeight(item.getWeight());
        armor.setCondition(item.getCondition());
        armor.setBonus(item.getBonus());
        return armor;
    }
}
