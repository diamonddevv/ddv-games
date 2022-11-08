package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Collection;

public abstract class Minigame {

    private final MutableText name;
    private PlayerEntity winner;
    private boolean running;

    protected Minigame(MutableText name) {
        this.running = false;
        this.name = name;
        this.winner = null;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean canStart(Collection<PlayerEntity> players) {
        return players.size() > 1;
    }

    public void start(Collection<PlayerEntity> players) {
        if (this.canStart(players)) {
            this.winner = null;
            this.running = true;
            runClock(players);
        }
    }
    public void end() {
        if (this.isRunning()) {
            this.running = false;
        }
    }


    public Text getName() {
        return this.name;
    }
    private void runClock(Collection<PlayerEntity> players) {
        while (running) {
            onRunClockCycle(players);
            this.winner = checkWin(players);
            if (this.winner != null) {
                DDVGamesMod.gameManager.declareWinner(this.winner);
            }
        }
    }

    public abstract PlayerEntity checkWin(Collection<PlayerEntity> players);

    public abstract void onPlayerWin(PlayerEntity player);

    public abstract void onRunClockCycle(Collection<PlayerEntity> players);

}
