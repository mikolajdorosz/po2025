package org.example.thewitcher.service;

import org.example.thewitcher.model.game.Game;

import java.io.*;

public class GameStateManager {
    private static final String SAVE_FILE = "save.dat";

    public static void saveGame(Game game) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(game);
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public static Game loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Game) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    public static void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
