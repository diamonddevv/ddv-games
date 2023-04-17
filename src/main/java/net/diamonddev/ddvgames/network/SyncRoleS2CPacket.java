package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SyncRoleS2CPacket implements NerveS2CPacket<SyncRoleS2CPacket, SyncRoleS2CPacket.SyncRoleData> {

    @Override
    public ClientPlayNetworking.PlayChannelHandler receive(Identifier channel) {
        return (client, handler, buf, responseSender) -> {
            SyncRoleData data = read(buf);

            Role role = Role.fromName(data.roleName);
            PlayerEntity player = handler.getWorld().getPlayerByUuid(data.playerUUID);

            client.execute(() -> DDVGamesClient.HASHED_ROLES.put(player, role));
        };
    }

    @Override
    public PacketByteBuf write(SyncRoleData data) {
        PacketByteBuf buf = getNewBuf();

        buf.writeString(data.roleName);
        buf.writeUuid(data.playerUUID);

        return buf;
    }

    @Override
    public SyncRoleData read(PacketByteBuf buf) {
        SyncRoleData data = new SyncRoleData();

        data.roleName = buf.readString();
        data.playerUUID = buf.readUuid();

        return data;
    }

    public static class SyncRoleData extends NervePacketData {
        public String roleName;
        public UUID playerUUID;
    }
}
