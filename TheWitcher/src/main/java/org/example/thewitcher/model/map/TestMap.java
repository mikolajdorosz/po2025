package org.example.thewitcher.model.map;

import org.example.thewitcher.model.entity.player.Player;

import java.io.InputStream;

public class TestMap extends Location {
    public TestMap(Player player) {
        super(player, loadFile());
        // Set player spawn point left of the first NPC (Armorer is at 15,4)
        player.setX(12);
        player.setY(4);
    }

    private static InputStream loadFile() {
        InputStream is = TestMap.class.getResourceAsStream("/org/example/thewitcher/maps/test_map.txt");
        if (is == null) throw new IllegalStateException("Map file not found!");
        return is;
    }
}
