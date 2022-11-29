package net.diamonddev.ddvgames.client.network;

import net.diamonddev.ddvgames.NetcodeConstants;
import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.network.*;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class ClientPacketRecievers implements RegistryInitializer {
    @Override
    public void register() {
        // Lives Sync
        ClientPlayNetworking.registerGlobalReceiver(NetcodeConstants.SYNC_LIVES_ID, (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncLivesS2CPacket.SyncLivesPacketData data = SyncLivesS2CPacket.read(buf);

            int lives = data.lives;
            PlayerEntity player = handler.getWorld().getPlayerByUuid(data.playerUUID);


            client.execute(() -> DDVGamesClient.HASHED_LIVES.put(player, lives));
        });

        // Role Sync
        ClientPlayNetworking.registerGlobalReceiver(NetcodeConstants.SYNC_ROLES_ID, (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncRoleS2CPacket.SyncRolesPacketData data = SyncRoleS2CPacket.read(buf);

            Role role = Role.fromName(data.roleName);
            PlayerEntity player = handler.getWorld().getPlayerByUuid(data.playerUUID);

            client.execute(() -> DDVGamesClient.HASHED_ROLES.put(player, role));
        });

        // Game Sync
        ClientPlayNetworking.registerGlobalReceiver(NetcodeConstants.SYNC_GAME, (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncGameS2CPacket.SyncGamePacketData data = SyncGameS2CPacket.read(buf);

            Minigame game = InitRegistries.MINIGAMES.get(data.gameId);
            boolean gameRunning = data.isRunning;

            client.execute(() -> {
                DDVGamesClient.CURRENT_GAME = game;
                DDVGamesClient.IS_GAME_STARTED = gameRunning;
            });
        });

        // State Sync
        ClientPlayNetworking.registerGlobalReceiver(NetcodeConstants.SYNC_STATE, (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncGameStateS2CPacket.SyncStatePacketData data = SyncGameStateS2CPacket.read(buf);

            String stateName = data.stateName;

            client.execute(() -> DDVGamesClient.CURRENT_STATE_NAME = stateName);
        });

        // Playercount Sync
        ClientPlayNetworking.registerGlobalReceiver(NetcodeConstants.SYNC_PLAYERCOUNT, (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncPlayersS2CPacket.SyncPlayersPacketData data = SyncPlayersS2CPacket.read(buf);

            int i = data.playercount;

            client.execute(() -> DDVGamesClient.CURRENT_PLAYERCOUNT = i);
        });
    }
}
