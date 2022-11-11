package net.diamonddev.ddvgames.minigame;

import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;

public class GameManager {

    private static GameManager manager;
    private Collection<Setting> settings;
    private Minigame game;
    private Collection<PlayerEntity> players;
    public PlayerEntity winner;

    private GameManager() {
        this.game = null;
        this.players = new ArrayList<>();
        this.winner = null;
        this.settings = new ArrayList<>();
    }
    public static GameManager getGameManager() {
        if (manager == null) {
            manager = new GameManager();
        }
        return manager;
    }

    // ########## \\

    public boolean isGameRunning() {
        if (game != null) {
            return game.isRunning();
        }
        return false;
    }
    public boolean isGameRunning(Minigame game) {
        if (this.game != null && game != null) {
            if (this.game == game) {
                return this.isGameRunning();
            }
        }
        return false;
    }
    public void startGame() {
        if (game != null) {
            game.start(this.players);
        }
    }
    public void stopGame() {
        if (game.isRunning()) {
            game.end();
        }
    }

    public void setGame(Minigame game) {
        if (!this.isGameRunning()) {
            this.game = game;
            addGameSettingsToList(game.getSettings());
        }
    }

    public boolean hasGame() {
        return this.game != null;
    }

    private void addGameSettingsToList(ArrayList<Setting> gameSettings) {
        this.settings = gameSettings;
    }
    public Minigame getGame() {
        return this.game;
    }

    public void addPlayer(PlayerEntity player) {
        this.players.add(player);
    }
    public void addPlayers(Collection<PlayerEntity> players) {
        players.addAll(this.players);
    }

    public Collection<Setting> getSettings() {
        return settings;
    }

    public double getSetting(String simpleName) {
        for (Setting s : settings) {
            if (s.getSimpleName().matches(simpleName)) {
                return s.getValue();
            }
        }
        return -1;
    }

    public void setSetting(String simpleName, double val) {
        for (Setting s : settings) {
            if (s.getSimpleName().matches(simpleName)) {
                s.setValue(val);
            }
        }
    }
}
