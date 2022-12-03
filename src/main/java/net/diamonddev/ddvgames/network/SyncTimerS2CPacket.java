package net.diamonddev.ddvgames.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class SyncTimerS2CPacket {
    public static PacketByteBuf write(boolean shouldTick) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(shouldTick);

        return buf;
    }

    public static SyncTimePacketData read(PacketByteBuf buf) {
        SyncTimePacketData data = new SyncTimePacketData();

        data.shouldTick = buf.readBoolean();

        return data;
    }


    public static class SyncTimePacketData {
        public boolean shouldTick;
    }
}
