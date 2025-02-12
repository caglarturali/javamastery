package com.cardgame.config;

public class DisplayConfig {
    public enum DisplayMode {
        DEBUG,
        PLAYER
    }

    private static DisplayMode currentMode = DisplayMode.PLAYER;

    public static DisplayMode getMode() {
        return currentMode;
    }

    public static void setMode(DisplayMode mode) {
        currentMode = mode;
    }

    public static DisplayMode toggleMode() {
        currentMode = currentMode == DisplayMode.DEBUG
                ? DisplayMode.PLAYER
                : DisplayMode.DEBUG;
        return currentMode;
    }
}
