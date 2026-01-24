package org.example.thewitcher.model.entity.player;

import org.example.thewitcher.model.items.Item;
import org.example.thewitcher.model.items.Steel;

public class SteelPlayerDecorator extends PlayerDecorator {
    private Steel steel;

    public SteelPlayerDecorator(Player player, Item steel) {
        super(player);
        getEquipment().removeItem(steel);
        this.steel = new Steel(steel.getName());
        this.steel = Steel.parse(steel);
        equip();
    }

    @Override
    public void equip() {
        if (getAppliedEquipment().getSteel() != null) {
            takeOff(player, getAppliedEquipment().getSteel());
        }
        getAppliedEquipment().setSteel(steel);
        getAppliedEquipment().addToList(steel.getName().toLowerCase());
    }
}
