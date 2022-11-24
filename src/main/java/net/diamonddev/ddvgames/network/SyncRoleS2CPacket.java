package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.minigame.Role;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class SyncRoleS2CPacket {

    public static PacketByteBuf write(Role role, PlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeString(role.getName());
        buf.writeUuid(player.getUuid());

        return buf;
    }

    public static SyncRolesPacketData read(PacketByteBuf buf) {
        SyncRolesPacketData data = new SyncRolesPacketData();

        data.roleName = buf.readString();
        data.playerUUID = buf.readUuid();

        return data;
    }


    public static class SyncRolesPacketData {
        public String roleName;
        public UUID playerUUID;
    }
}
