package net.diamonddev.ddvgames;

import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.network.SyncPlayersS2CPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class EventListeners {
    public static void onDisconnectServer() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            DDVGamesMod.gameManager.getPlayers().remove(handler.player);
            DDVGamesEntityComponents.setRole(handler.player, Role.EMPTY);
            DDVGamesMod.gameManager.getPlayers().forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, NetcodeConstants.SYNC_PLAYERCOUNT, SyncPlayersS2CPacket.write(DDVGamesMod.gameManager.getPlayers().size())));
        });
    }
    public static void onWorldTickClient() {
        ClientTickEvents.START_WORLD_TICK.register((world) -> {
            if (DDVGamesClient.TIMER_ENABLED) DDVGamesClient.TIMER++;
        });
    }

    public static void onDisconnectClient() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            DDVGamesClient.TIMER_ENABLED = false;
            DDVGamesClient.TIMER = 0;
            DDVGamesClient.CURRENT_PLAYERCOUNT = 0;
            DDVGamesClient.VOID_LEVEL = 0;
            DDVGamesClient.CURRENT_STATE_NAME = "";

            DDVGamesClient.IS_GAME_STARTED = false;
        });
    }
}
