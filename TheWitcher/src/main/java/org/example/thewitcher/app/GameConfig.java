package org.example.thewitcher.app;

public record GameConfig(String title, int windowWidth, int windowHeight) {
    public static GameConfig defaultConfig() {
        return new GameConfig("The Witcher",640,480);
    }
    public String getTitle() { return title; }
    public int getWindowWidth() { return windowWidth; }
    public int getWindowHeight() { return windowHeight; }
}
