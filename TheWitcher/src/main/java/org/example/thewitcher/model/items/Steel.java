package org.example.thewitcher.model.items;

public class Steel extends Weapon {
    public Steel(String name) {
        super(name);
        this.type = "steel";
    }

    public static Steel parse(Item item) {
        Steel weapon = new Steel(item.getName());
        weapon.setType(item.getType());
        weapon.setPrice(item.getPrice());
        weapon.setWeight(item.getWeight());
        weapon.setCondition(item.getCondition());
        weapon.setBonus(item.getBonus());
        return weapon;
    }
}
