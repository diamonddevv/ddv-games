package net.diamonddev.ddvgames.minigame;


import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Minigame {

    private final MutableText name;
    private final String version;
    private final SemanticVersioningSuffix verSuffix;
    private ArrayList<GameState> states;
    private long tickClock;
    private ArrayList<Role> roles;
    private ArrayList<Setting> settings;
    private PlayerEntity winner;
    private boolean running;
    public GameState currentState;
    public GameState previousState;
    protected Minigame(MutableText name, String semanticVersion, SemanticVersioningSuffix versioningSuffix) {
        this.running = false;
        this.name = name;

        this.winner = null;

        this.settings = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.states = new ArrayList<>();
        this.settings = addSettings(this.settings);
        this.roles = addRoles(this.roles);
        this.states = addGameStates(this.states);

        this.version = semanticVersion;
        this.verSuffix = versioningSuffix;

        this.tickClock = 0;
    }

    public boolean isRunning() {
        return running;
    }

    public abstract ArrayList<Role> addRoles(ArrayList<Role> roles);
    public abstract ArrayList<Setting> addSettings(ArrayList<Setting> settings);

    public abstract ArrayList<GameState> addGameStates(ArrayList<GameState> states);
    public abstract void onStart(Entity executor, Collection<PlayerEntity> players, World world);
    public abstract void onEnd(Collection<PlayerEntity> players, World world);

    public abstract boolean canWin(PlayerEntity winnerCandidate, Collection<PlayerEntity> players);
    public abstract void onWin(PlayerEntity winningPlayer, World world, Collection<PlayerEntity> players);

    public abstract void tickClock(World world);

    public long getTicks() {
        return this.tickClock;
    }

    public ArrayList<Setting> getSettings() {return this.settings;}
    public ArrayList<Role> getRoles() {return this.roles;}

    public ArrayList<GameState> getStates() {
        return states;
    }

    public boolean canStart(Collection<PlayerEntity> players) {
        return players.size() > 1;
    }

    public void start(Entity executor, Collection<PlayerEntity> players, World world) {

        if (world.getRegistryKey() != World.OVERWORLD) {
            executor.sendMessage(Text.translatable("ddv.minigame.start.unsupportedDimension"));
            return;
        }

        if (this.canStart(players)) {
            this.winner = null;
            this.running = true;
            this.currentState = GameState.fromName(getStartingStateName());
            this.tickClock = 0;
            this.onStart(executor, players, world);
            DDVGamesMod.gameManager.switchState(currentState, world);
        }
    }
    public void end(Collection<PlayerEntity> players, World world) {
        if (this.isRunning()) {
            this.running = false;
            this.onEnd(players, world);
        }
    }

    public void tryWin(World world) {
        if (this.isRunning()) {
            for (PlayerEntity player : DDVGamesMod.gameManager.getPlayers()) {
                if (canWin(player, DDVGamesMod.gameManager.getPlayers())) {
                    this.winner = player;
                    onWin(player, this.winner.world, DDVGamesMod.gameManager.getPlayers());
                    end(DDVGamesMod.gameManager.getPlayers(), world);
                    this.running = false;
                }
            }
        }
    }

    public PlayerEntity getWinner() {
        return winner;
    }

    public abstract String getDefaultRoleName();
    public abstract String getStartingStateName();

    public abstract void onStateStarts(GameState state, World world);
    public abstract void onStateEnds(GameState state, World world);

    public void togglePause() {
        this.running = !this.running;
    }
    public void changeTickClock() {
        tickClock++;
    }
    public Text getName() {
        return this.name;
    }
    public Text getSemanticVersion() {
        MutableText text = Text.translatable("ddv.semanticVersioning.prefix");
        text.append(Text.literal(this.version));
        text.append(Text.translatable(this.verSuffix.getTranslationKey()));

        return text;
    }

}
