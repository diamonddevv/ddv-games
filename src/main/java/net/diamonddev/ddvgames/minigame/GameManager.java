package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public class GameManager {

    private static GameManager manager;

    private Collection<Setting> settings;
    private Collection<Role> roles;
    private Minigame game;
    private Collection<PlayerEntity> players;
    public PlayerEntity winner;

    private GameManager() {
        this.game = null;
        this.players = new ArrayList<>();
        this.winner = null;
        this.settings = new ArrayList<>();
        this.roles = new ArrayList<>();
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
            game.start(executor, this.players, world);
        }
    }
    public void stopGame(World world) {
        if (game.isRunning()) {
            game.end(this.players, world);
        }
    }

    public void setGame(Minigame game) {
        if (!this.isGameRunning()) {
            this.game = game;
            addGameSettingsToList(game.getSettings());
            addGameRolesToList(game.getRoles());
        }
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
    public Minigame getGame() {
        return this.game;
    }

    public Collection<PlayerEntity> getPlayers() {
        return players;
    }
    public Collection<PlayerEntity> getPlayersWithRole(Role role) {
        players.removeIf(player -> !DDVGamesEntityComponents.getRole(player).getName().matches(role.getName()));
        return players;
    }

    public void addPlayers(Collection<ServerPlayerEntity> players) {
        this.players.addAll(players);
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

    @Deprecated
    public static class GameManagerBuilder {

        private GameManager manager;

        private GameManagerBuilder() {
            this.manager = new GameManager();
        }

        public static GameManagerBuilder start() {
            return new GameManagerBuilder();
        }

        public GameManager build() {
            return this.manager;
        }


        public GameManagerBuilder setGame(Minigame game) {
            this.manager.setGame(game);
            return this;
        }

        public GameManagerBuilder setPlayerList(Collection<PlayerEntity> players) {
            this.manager.getPlayers().clear();
            this.manager.getPlayers().addAll(players);
            return this;
        }

    }
}
