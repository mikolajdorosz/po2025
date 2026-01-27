package org.example.thewitcher.model.items;

import java.util.ArrayList;
import java.util.List;

public class Equipment implements java.io.Serializable {
    private String ownerName;
    private List<Item> items;

    public Equipment(String ownerName) {
        this.ownerName = ownerName;
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() { return items; }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(name)) {
                items.remove(i);
                return;
            }
        }
    }

    public Item select(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Do dodania (może?) - metody np. liczące itemy do questów
}