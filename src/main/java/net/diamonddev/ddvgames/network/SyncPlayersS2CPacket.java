package net.diamonddev.ddvgames.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class SyncPlayersS2CPacket {
    public static PacketByteBuf write(int playercount) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(playercount);
        return buf;
    }

    public static SyncPlayersPacketData read(PacketByteBuf buf) {
        SyncPlayersPacketData data = new SyncPlayersPacketData();
        data.playercount = buf.readInt();
        return data;
    }


    public static class SyncPlayersPacketData {
        public int playercount;
    }
}
