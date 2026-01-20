package org.example.thewitcher.model.items;

public class Distance extends Weapon {
    public Distance(String name) {
        super(name);
        this.type = "distance";
    }

    public static Distance parse(Item item) {
        Distance weapon = new Distance(item.getName());
        weapon.setType(item.getType());
        weapon.setPrice(item.getPrice());
        weapon.setWeight(item.getWeight());
        weapon.setCondition(item.getCondition());
        weapon.setBonus(item.getBonus());
        return weapon;
    }
}
