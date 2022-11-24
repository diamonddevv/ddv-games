package net.diamonddev.ddvgames.client;

import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.network.NetcodeConstants;
import net.diamonddev.ddvgames.network.SyncLivesS2CPacket;
import net.diamonddev.ddvgames.network.SyncRoleS2CPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public class DDVGamesClient implements ClientModInitializer {

    public static final HashMap<PlayerEntity, Integer> HASHED_LIVES = new HashMap<>();
    public static final HashMap<PlayerEntity, Role> HASHED_ROLES = new HashMap<>();
    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(NetcodeConstants.SYNC_LIVES_ID, (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncLivesS2CPacket.SyncLivesPacketData data = SyncLivesS2CPacket.read(buf);

            int lives = data.lives;
            PlayerEntity player = handler.getWorld().getPlayerByUuid(data.playerUUID);


           client.execute(() -> HASHED_LIVES.put(player, lives));
        });

        ClientPlayNetworking.registerGlobalReceiver(NetcodeConstants.SYNC_ROLES_ID, (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncRoleS2CPacket.SyncRolesPacketData data = SyncRoleS2CPacket.read(buf);

            Role role = Role.fromName(data.roleName);
            PlayerEntity player = handler.getWorld().getPlayerByUuid(data.playerUUID);

            client.execute(() -> HASHED_ROLES.put(player, role));
        });

    }
}
