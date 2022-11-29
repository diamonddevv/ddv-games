package net.diamonddev.ddvgames.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class SyncGameStateS2CPacket {
    public static PacketByteBuf write(String stateName) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(stateName);
        return buf;
    }

    public static SyncStatePacketData read(PacketByteBuf buf) {
        SyncStatePacketData data = new SyncStatePacketData();
        data.stateName = buf.readString();
        return data;
    }


    public static class SyncStatePacketData {
        public String stateName;
    }
}
