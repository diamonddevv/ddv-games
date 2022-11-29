package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
public class SyncGameS2CPacket {
    public static PacketByteBuf write(Minigame game, boolean isRunning) {
        PacketByteBuf buf = PacketByteBufs.create();

        Identifier id = InitRegistries.MINIGAMES.getId(game);

        buf.writeIdentifier(id);
        buf.writeBoolean(isRunning);


        return buf;
    }

    public static SyncGamePacketData read(PacketByteBuf buf) {
        SyncGamePacketData data = new SyncGamePacketData();

        data.gameId = buf.readIdentifier();
        data.isRunning = buf.readBoolean();

        return data;
    }


    public static class SyncGamePacketData {
        public Identifier gameId;
        public boolean isRunning;
    }
}
