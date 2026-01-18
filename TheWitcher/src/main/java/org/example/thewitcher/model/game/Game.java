package org.example.thewitcher.model.game;

import org.example.thewitcher.model.entity.Player;
import org.example.thewitcher.model.map.Location;
import org.example.thewitcher.model.map.Velen;

public class Game {
    private Player player;
    private Location location;

    public Game() {
        player = new Player();
        location = new Velen(player);
    }

    public Player getPlayer() { return player; }
    public Location getLocation() { return location; }

    public void movePlayer(int deltaX, int deltaY) {
        int newX = player.getX() + deltaX;
        int newY = player.getY() + deltaY;

        if (newX < 0 || newY < 0 || newX >= location.getWidth() || newY >= location.getHeight()) return;
        if (!location.getPoint(newX, newY).isObstacle()) {
            player.setX(newX);
            player.setY(newY);
        }
    }
}
