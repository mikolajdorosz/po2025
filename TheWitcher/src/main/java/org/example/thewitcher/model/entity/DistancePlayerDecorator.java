package org.example.thewitcher.model.entity;

import org.example.thewitcher.model.items.Distance;
import org.example.thewitcher.model.items.Item;

public class DistancePlayerDecorator extends PlayerDecorator {
    private Distance distance;

    public DistancePlayerDecorator(Player player, Item distance) {
        super(player);
        getEquipment().removeItem(distance);
        this.distance = new Distance(distance.getName());
        this.distance = Distance.parse(distance);
        equip();
    }

    @Override
    public void equip() {
        if (getAppliedEquipment().getDistance() != null) {
            takeOff(player, getAppliedEquipment().getDistance());
        }
        getAppliedEquipment().setDistance(distance);
        getAppliedEquipment().addToList(distance.getName().toLowerCase());
    }
}
