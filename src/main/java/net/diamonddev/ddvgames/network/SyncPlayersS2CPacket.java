package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncPlayersS2CPacket implements NerveS2CPacket<SyncPlayersS2CPacket, SyncPlayersS2CPacket.SyncPlayersData> {

    @Override
    public ClientPlayNetworking.PlayChannelHandler receive(Identifier channel) {
        return (client, handler, buf, responseSender) -> {
            SyncPlayersData data = read(buf);

            int i = data.playercount;

            client.execute(() -> DDVGamesClient.CURRENT_PLAYERCOUNT = i);
        };
    }

    @Override
    public PacketByteBuf write(SyncPlayersData data) {
        PacketByteBuf buf = getNewBuf();

        buf.writeInt(data.playercount);

        return buf;
    }

    @Override
    public SyncPlayersData read(PacketByteBuf buf) {
        SyncPlayersData data = new SyncPlayersData();

        data.playercount = buf.readInt();

        return data;
    }

    public static class SyncPlayersData extends NervePacketData {
        public int playercount;
    }
}
