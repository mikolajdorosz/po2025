package org.example.thewitcher.model.items;

import org.example.thewitcher.model.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class AppliedEquipment {
    private List<String> items;
    private Silver silver;
    private Steel steel;
    private Distance distance;
    private Armor armor;

    public AppliedEquipment() {
        this.items = new ArrayList<>();
    }

    public List<String> getItems() { return items; }

    public Silver getSilver() { return silver; }
    public void setSilver(Silver silver) { this.silver = silver; }

    public Steel getSteel() { return steel; }
    public void setSteel(Steel steel) { this.steel = steel; }

    public Distance getDistance() { return distance; }
    public void setDistance(Distance distance) { this.distance = distance; }

    public Armor getArmor() { return armor; }
    public void setArmor(Armor armor) { this.armor = armor; }

    public void addToList(String name) {
        items.add(name);
    }

    public void removeFromList(String name) {
        items.remove(name);
    }

    public IWearable select(Player p, String name) {
        if (!p.getAppliedEquipment().getItems().contains(name)) return null;

        if (p.getAppliedEquipment().getSilver() != null && name.equals(p.getAppliedEquipment().getSilver().getName().toLowerCase())) return silver;
        if (p.getAppliedEquipment().getSteel() != null && name.equals(p.getAppliedEquipment().getSteel().getName().toLowerCase())) return steel;
        if (p.getAppliedEquipment().getDistance() != null && name.equals(p.getAppliedEquipment().getDistance().getName().toLowerCase())) return distance;
        if (p.getAppliedEquipment().getArmor() != null && name.equals(p.getAppliedEquipment().getArmor().getName().toLowerCase())) return armor;

        return null;
    }

}
