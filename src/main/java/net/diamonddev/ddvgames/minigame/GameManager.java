package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.NetcodeConstants;
import net.diamonddev.ddvgames.network.SyncGameS2CPacket;
import net.diamonddev.ddvgames.network.SyncGameStateS2CPacket;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class GameManager {

    private static GameManager manager;
    private boolean running;

    private Collection<Setting> settings;
    private Collection<Role> roles;
    private Collection<GameState> states;
    private Minigame game;
    private final Collection<PlayerEntity> players;
    public PlayerEntity winner;

    public GameState currentState;
    public GameState previousState;

    private GameManager() {
        this.game = null;
        this.players = new ArrayList<>();
        this.winner = null;
        this.settings = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.states = new ArrayList<>();
        this.running = false;
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

    public void startGame(Entity executor, World world) {
        if (game != null) {
            this.running = true;
            game.start(executor, this.players, world);

            this.players.forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, NetcodeConstants.SYNC_GAME, SyncGameS2CPacket.write(game, true)));
        }
    }

    public boolean getGameHasStarted() {
        return this.running;
    }
    public boolean getSpecificGameHasStarted(Minigame game) {
        if (this.game == game) {
            return this.running;
        } else {
            return false;
        }
    }
    public void stopGame(World world) {
        if (game.isRunning()) {
            this.running = false;
            game.end(this.players, world);

            this.players.forEach(player -> DDVGamesEntityComponents.setRole(player, Role.EMPTY));
            this.players.forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, NetcodeConstants.SYNC_GAME, SyncGameS2CPacket.write(game, false)));
        }
    }

    public void setGame(Minigame game) {
        if (!this.isGameRunning()) {
            this.game = game;
            addGameSettingsToList(game.getSettings());
            addGameRolesToList(game.getRoles());
            addGameStatesToList(game.getStates());
        }
    }

    public void tick() {
        this.winner = this.hasGame() ? this.getGame().getWinner() : null;
    }

    public boolean hasGame() {
        return this.game != null;
    }

    private void addGameSettingsToList(ArrayList<Setting> gameSettings) {
        this.settings = gameSettings;
    }

    private void addGameRolesToList(ArrayList<Role> gameRoles) {
        this.roles = gameRoles;
    }

    private void addGameStatesToList(ArrayList<GameState> gameStates) {
        this.states = gameStates;
    }

    public Minigame getGame() {
        return this.game;
    }

    public Collection<PlayerEntity> getPlayers() {
        return players;
    }

    public Collection<PlayerEntity> getPlayersWithRole(Role role) {
        Collection<PlayerEntity> roledPlayers = new ArrayList<>();
        players.forEach(player -> {
            if (DDVGamesEntityComponents.getRoleName(player).matches(role.getName())) roledPlayers.add(player);
        });
        return roledPlayers;
    }

    public PlayerEntity getWinner() {
        return winner;
    }

    public void addPlayers(Collection<ServerPlayerEntity> players) {
        this.players.addAll(players);
    }

    public void addPlayersWithRole(Collection<ServerPlayerEntity> players, Role role) {
        this.players.clear();
        this.players.addAll(players);
        players.forEach(player -> attachRole(player, role));
    }

    public void removePlayers(Collection<ServerPlayerEntity> players) {
        this.players.removeAll(players);
    }

    public void attachRole(PlayerEntity player, Role role) {
        detachRole(player);
        DDVGamesEntityComponents.setRole(player, role);
    }

    public void detachRole(PlayerEntity player) {
        DDVGamesEntityComponents.setRole(player, Role.EMPTY);
    }
    public Collection<Setting> getSettings() {
        return settings;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public ArrayList<String> getRolesAsSimpleNames() {
        ArrayList<Role> roles = getGame().getRoles();
        ArrayList<String> simpleNamedRoles = new ArrayList<>();

        for (Role role : roles) {
            simpleNamedRoles.add(role.getName());
        }
        return simpleNamedRoles;
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

    public void setAllSettings(SettingsSet set) {
        if (set.getId().toString().matches(getCurrentGameId().toString())) {
            for (Map.Entry<String, Double> pair : set.getKeys().entrySet()) {
                setSetting(pair.getKey(), pair.getValue());
            }
        }
    }

    public Identifier getCurrentGameId() {
        if (this.game != null) {
            return InitRegistries.MINIGAMES.getId(this.game);
        } else throw new NullPointerException("Current game is null, could not get registry ID");
    }

    public GameState getCurrentState() {
        return this.getGameHasStarted() ? currentState : null;
    }

    public Collection<GameState> getStates() {
        return states;
    }

    public void switchState(GameState newState, World world) {
        if (isGameRunning()) {
            previousState = currentState;
            currentState = newState;
            this.game.currentState = currentState;
            this.game.previousState = previousState;
            game.onStateStarts(currentState, world);
            game.onStateEnds(previousState, world);

            this.players.forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, NetcodeConstants.SYNC_STATE, SyncGameStateS2CPacket.write(newState.getName())));
        }
    }

    public Role getDefaultRole() {
        if (this.hasGame()) {
            return Role.fromName(this.game.getDefaultRoleName());
        } else return Role.EMPTY;
    }

    public double getTimer() {
        return this.game != null ? this.game.timer / 10.0 : 0.0;
    }
}