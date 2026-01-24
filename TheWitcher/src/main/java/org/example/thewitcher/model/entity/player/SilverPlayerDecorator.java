package org.example.thewitcher.model.entity.player;

import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.items.Silver;

public class SilverPlayerDecorator extends PlayerDecorator {
    private Silver silver;

    public SilverPlayerDecorator(Player player, Item silver) {
        super(player);
        getEquipment().removeItem(silver);
        this.silver = new Silver(silver.getName());
        this.silver = Silver.parse(silver);
        equip();
    }

    @Override
    public void equip() {
        if (getAppliedEquipment().getSilver() != null) {
            takeOff(player, getAppliedEquipment().getSilver());
        }
        getAppliedEquipment().setSilver(silver);
        getAppliedEquipment().addToList(silver.getName().toLowerCase());
    }
}
