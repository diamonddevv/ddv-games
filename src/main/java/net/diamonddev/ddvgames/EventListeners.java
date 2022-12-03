package net.diamonddev.ddvgames;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventListeners {
    public static void onDisconnectServer() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> DDVGamesMod.gameManager.getPlayers().remove(handler.player));
    }

    public static void onWorldTickClient() {
        ClientTickEvents.START_WORLD_TICK.register((world) -> {
            if (DDVGamesClient.TIMER_ENABLED) DDVGamesClient.TIMER++;
        });
    }
}
