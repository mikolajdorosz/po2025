package org.example.thewitcher.model.items;

public class Silver extends Weapon {
    public Silver(String name) {
        super(name);
        this.type = "silver";
    }

    public static Silver parse(Item item) {
        Silver weapon = new Silver(item.getName());
        weapon.setType(item.getType());
        weapon.setPrice(item.getPrice());
        weapon.setWeight(item.getWeight());
        weapon.setCondition(item.getCondition());
        weapon.setBonus(item.getBonus());
        return weapon;
    }
}