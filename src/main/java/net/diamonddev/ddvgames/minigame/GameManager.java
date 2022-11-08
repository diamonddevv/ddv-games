package net.diamonddev.ddvgames.minigame;

import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;

public class GameManager {

    private static GameManager manager;

    private Minigame game;
    private Collection<PlayerEntity> players;
    public PlayerEntity winner;

    private GameManager() {
        this.game = null;
        this.players = new ArrayList<>();
        this.winner = null;
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
        }
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

    public void declareWinner(PlayerEntity player) {
        if (this.players.contains(player)) {
            this.winner = player;
            game.onPlayerWin(player);
        }
    }

}
