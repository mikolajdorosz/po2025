package org.example.thewitcher.config;

public class GameConfig {
    private final int windowWidth;
    private final int windowHeight;
    private final int pointWidth;
    private final int pointHeight;
    private final String title;
    private final String fontName;
    private final int fontSize;

    private GameConfig(Builder builder) {
        this.windowWidth = builder.windowWidth;
        this.windowHeight = builder.windowHeight;
        this.pointWidth = builder.pointWidth;
        this.pointHeight = builder.pointHeight;
        this.fontName = builder.fontName;
        this.fontSize = builder.fontSize;
        this.title = builder.title;
    }

    public int getWindowWidth() { return windowWidth; }
    public int getWindowHeight() { return windowHeight; }
    public int getPointWidth() { return pointWidth; }
    public int getPointHeight() { return pointHeight; }
    public String getFontName() { return fontName; }
    public int getFontSize() { return fontSize; }
    public String getTitle() { return title; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private int windowWidth = 640;
        private int windowHeight = 480;
        private int pointWidth = 18;
        private int pointHeight = 24;
        private String fontName = "Consolas";
        private int fontSize = 20;
        private String title = "The Witcher";

        public Builder windowWidth(int width) {
            this.windowWidth = width;
            return this;
        }
        public Builder windowHeight(int height) {
            this.windowHeight = height;
            return this;
        }
        public Builder pointWidth(int width) {
            this.pointWidth = width;
            return this;
        }
        public Builder pointHeight(int height) {
            this.pointHeight = height;
            return this;
        }
        public Builder fontName(String fontName) {
            this.fontName = fontName;
            return this;
        }
        public Builder fontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public GameConfig build() {
            return new GameConfig(this);
        }
    }
}
