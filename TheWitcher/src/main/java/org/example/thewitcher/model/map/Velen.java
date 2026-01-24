package org.example.thewitcher.model.map;

import org.example.thewitcher.model.entity.player.Player;

import java.io.InputStream;

public class Velen extends Location {
    public Velen(Player player) { super(player, loadFile()); }

    private static InputStream loadFile() {
        InputStream is = Velen.class.getResourceAsStream("/org/example/thewitcher/maps/velen.txt");
        if (is == null) throw new IllegalStateException("Map file not found!");
        return is;
    }
}
