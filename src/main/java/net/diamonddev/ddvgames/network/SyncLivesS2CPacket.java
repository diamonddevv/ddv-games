package net.diamonddev.ddvgames.network;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class SyncLivesS2CPacket {

    public static PacketByteBuf write(int livesCount, PlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(livesCount);
        buf.writeUuid(player.getUuid());


        return buf;
    }

    public static SyncLivesPacketData read(PacketByteBuf buf) {
        SyncLivesPacketData data = new SyncLivesPacketData();
        data.lives = buf.readInt();
        data.playerUUID = buf.readUuid();
        return data;
    }


    public static class SyncLivesPacketData {
        public int lives;
        public UUID playerUUID;
    }
}
