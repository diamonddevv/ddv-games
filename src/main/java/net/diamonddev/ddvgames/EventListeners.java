package net.diamonddev.ddvgames;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventListeners {

    public static void onDisconnectServer() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> DDVGamesMod.gameManager.getPlayers().remove(handler.player));
    }


}
