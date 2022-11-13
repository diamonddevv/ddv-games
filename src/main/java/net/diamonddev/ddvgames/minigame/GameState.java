package net.diamonddev.ddvgames.minigame;

public class GameState {

    private final String name;

    public GameState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static GameState fromName(String name) {
        return new GameState(name);
    }
}
