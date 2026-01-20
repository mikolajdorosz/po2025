package org.example.thewitcher.model.entity;

import org.example.thewitcher.model.items.Armor;
import org.example.thewitcher.model.items.Item;

public class ArmorPlayerDecorator extends PlayerDecorator {
    private Armor armor;

    public ArmorPlayerDecorator(Player player, Item armor) {
        super(player);
        this.armor = new Armor(armor.getName());
        this.armor = Armor.parse(armor);
        equip();
    }

    @Override
    public void equip() {
        if (getAppliedEquipment().getArmor() != null) {
            takeOff(player, getAppliedEquipment().getArmor());
        }
        getAppliedEquipment().setArmor(armor);
        getEquipment().removeItem(armor);
        getAppliedEquipment().addToList(armor.getName().toLowerCase());
        setArmor(getArmor() + getAppliedEquipment().getArmor().getBonus());
    }
}