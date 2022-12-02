package net.diamonddev.ddvgames.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class SyncVoidLevelS2CPacket {
    public static PacketByteBuf write(int voidlevel) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(voidlevel);
        return buf;
    }

    public static SyncVoidLevelPacketData read(PacketByteBuf buf) {
        SyncVoidLevelPacketData data = new SyncVoidLevelPacketData();
        data.voidlevel = buf.readInt();
        return data;
    }


    public static class SyncVoidLevelPacketData {
        public int voidlevel;
    }
}
