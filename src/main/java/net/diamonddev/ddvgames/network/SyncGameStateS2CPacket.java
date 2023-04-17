package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncGameStateS2CPacket implements NerveS2CPacket<SyncGameStateS2CPacket, SyncGameStateS2CPacket.SyncGameStateData> {
    @Override
    public ClientPlayNetworking.PlayChannelHandler receive(Identifier channel) {
        return (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncGameStateData data = read(buf);

            String stateName = data.stateName;

            client.execute(() -> DDVGamesClient.CURRENT_STATE_NAME = stateName);
        };
    }

    @Override
    public PacketByteBuf write(SyncGameStateData data) {
        PacketByteBuf buf = getNewBuf();

        buf.writeString(data.stateName);

        return buf;
    }

    @Override
    public SyncGameStateData read(PacketByteBuf buf) {
        SyncGameStateData data = new SyncGameStateData();

        data.stateName = buf.readString();

        return data;
    }

    public static class SyncGameStateData extends NervePacketData {
        public String stateName;
    }
}
