package net.diamonddev.ddvgames.minigame;


import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.network.SyncGameS2CPacket;
import net.diamonddev.ddvgames.network.SyncGameStateS2CPacket;
import net.diamonddev.ddvgames.network.SyncTimerS2CPacket;
import net.diamonddev.ddvgames.registry.InitPackets;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveNetworker;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
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

    public int timer;
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

        this.timer = 0;
    }

    public boolean isRunning() {
        return running;
    }

    public abstract ArrayList<Role> addRoles(ArrayList<Role> roles);
    public abstract ArrayList<Setting> addSettings(ArrayList<Setting> settings);

    public abstract ArrayList<GameState> addGameStates(ArrayList<GameState> states);
    public abstract void onStart(Entity executor, Collection<ServerPlayerEntity> players, World world);
    public abstract void onEnd(Collection<ServerPlayerEntity> players, World world);

    public abstract boolean canWin(ServerPlayerEntity winnerCandidate, Collection<ServerPlayerEntity> players);
    public abstract void onWin(ServerPlayerEntity winningPlayer, World world, Collection<ServerPlayerEntity> players);

    public abstract void tickClock(World world);

    public void essentialTicks(World world) {
        if (this.getTicks() % 2 == 0) {
            this.timer += 1;
        }
    }

    public long getTicks() {
        return this.tickClock;
    }

    public ArrayList<Setting> getSettings() {return this.settings;}
    public ArrayList<Role> getRoles() {return this.roles;}

    public ArrayList<GameState> getStates() {
        return states;
    }

    public boolean canStart() {
        return DDVGamesMod.gameManager.getPlayers().size() > 1;
    }

    public void start(Entity executor, Collection<ServerPlayerEntity> serverPlayers, World world) {

        if (world.getRegistryKey() != World.OVERWORLD) {
            executor.sendMessage(Text.translatable("ddv.minigame.start.unsupportedDimension"));
            return;
        }

        if (this.canStart()) {
            this.winner = null;
            this.running = true;
            this.currentState = GameState.fromName(getStartingStateName());
            this.tickClock = 0;
            this.onStart(executor, serverPlayers, world);

            // Network Timer
            serverPlayers.forEach(player -> {
                SyncTimerS2CPacket.SyncTimerData data = new SyncTimerS2CPacket.SyncTimerData();
                data.shouldTick = this.running;
                data.time = this.timer;

                NerveNetworker.send(player, InitPackets.SYNC_TIMER, data);
            });

            DDVGamesMod.gameManager.switchState(currentState, world);

            // Network Game Info
            serverPlayers.forEach(player ->{
                SyncGameS2CPacket.SyncGamePacketData data = new SyncGameS2CPacket.SyncGamePacketData();
                data.gameId = InitRegistries.MINIGAMES.getId(this);
                data.isRunning = true;

                NerveNetworker.send(player, InitPackets.SYNC_GAME, data);
            });
            serverPlayers.forEach(player -> {
                SyncGameStateS2CPacket.SyncGameStateData data = new SyncGameStateS2CPacket.SyncGameStateData();
                data.stateName = currentState.name();

                NerveNetworker.send(player, InitPackets.SYNC_STATE, data);
            });
        }
    }
    public void end(Collection<ServerPlayerEntity> players, World world) {
        if (this.isRunning()) {
            this.running = false;
            this.timer = 0;
            players.forEach(player -> {
                SyncTimerS2CPacket.SyncTimerData data = new SyncTimerS2CPacket.SyncTimerData();
                data.shouldTick = this.running;
                data.time = this.timer;

                NerveNetworker.send(player, InitPackets.SYNC_TIMER, data);
            });

            players.forEach(player ->{
                SyncGameS2CPacket.SyncGamePacketData data = new SyncGameS2CPacket.SyncGamePacketData();
                data.gameId = InitRegistries.MINIGAMES.getId(this);
                data.isRunning = true;

                NerveNetworker.send(player, InitPackets.SYNC_GAME, data);
            });

            this.onEnd(players, world);
        }
    }

    public void tryWin(World world) {
        if (this.isRunning()) {
            for (ServerPlayerEntity player : DDVGamesMod.gameManager.getPlayers()) {
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
        DDVGamesMod.gameManager.getPlayers().forEach(player -> {
            SyncTimerS2CPacket.SyncTimerData data = new SyncTimerS2CPacket.SyncTimerData();
            data.shouldTick = this.running;
            data.time = this.timer;

            NerveNetworker.send(player, InitPackets.SYNC_TIMER, data);
        });
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
