package net.diamonddev.ddvgames.client;

import net.diamonddev.ddvgames.EventListeners;
import net.diamonddev.ddvgames.client.network.ClientPacketRecievers;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.minigame.Role;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
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

    public static boolean TIMER_ENABLED = false;
    public static double TIMER = 0;

    // Rising Edge Specific Game Data
    public static int VOID_LEVEL = 0;
    @Override
    public void onInitializeClient() {
        new ClientPacketRecievers().register();

        EventListeners.onDisconnectClient();
        EventListeners.onWorldTickClient();
    }

    public static double getClientTimer() {
        return (TIMER / 10.0) * 0.5; // For some reason it was two times fast so i half it /shrug
    }

    public static boolean hasGameAndRunning(Minigame game) {
        return CURRENT_GAME == game && IS_GAME_STARTED;
    }
}
