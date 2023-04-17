package net.diamonddev.ddvgames.minigame;

public record GameState(String name) {

    public static GameState fromName(String name) {
        return new GameState(name);
    }
}
