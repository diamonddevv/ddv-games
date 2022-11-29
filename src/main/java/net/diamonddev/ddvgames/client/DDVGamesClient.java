package net.diamonddev.ddvgames.client;

import net.diamonddev.ddvgames.client.network.ClientPacketRecievers;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.minigame.Role;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public class DDVGamesClient implements ClientModInitializer {

    // Data Hashes
    public static final HashMap<PlayerEntity, Integer> HASHED_LIVES = new HashMap<>();
    public static final HashMap<PlayerEntity, Role> HASHED_ROLES = new HashMap<>();

    // Synced Game Data
    public static Minigame CURRENT_GAME;
    public static boolean IS_GAME_STARTED;
    public static int CURRENT_PLAYERCOUNT = 0;

    public static String CURRENT_STATE_NAME;
    @Override
    public void onInitializeClient() {
        new ClientPacketRecievers().register();
    }

    public static boolean hasGameAndRunning(Minigame game) {
        return CURRENT_GAME == game && IS_GAME_STARTED;
    }
}
