package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncVoidLevelS2CPacket implements NerveS2CPacket<SyncVoidLevelS2CPacket, SyncVoidLevelS2CPacket.SyncVoidLevelData> {

    @Override
    public ClientPlayNetworking.PlayChannelHandler receive(Identifier channel) {
        return (client, handler, buf, responseSender) -> {
            SyncVoidLevelData data = read(buf);

            int i = data.voidlevel;

            client.execute(() -> DDVGamesClient.VOID_LEVEL = i);
        };
    }

    @Override
    public PacketByteBuf write(SyncVoidLevelData data) {
        PacketByteBuf buf = getNewBuf();

        buf.writeInt(data.voidlevel);

        return buf;
    }

    @Override
    public SyncVoidLevelData read(PacketByteBuf buf) {
        SyncVoidLevelData data = new SyncVoidLevelData();

        data.voidlevel = buf.readInt();

        return data;
    }

    public static class SyncVoidLevelData extends NervePacketData {
        public int voidlevel;
    }
}
