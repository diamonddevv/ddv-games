package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncTimerS2CPacket implements NerveS2CPacket<SyncTimerS2CPacket, SyncTimerS2CPacket.SyncTimerData> {

    @Override
    public ClientPlayNetworking.PlayChannelHandler receive(Identifier channel) {
        return (client, handler, buf, responseSender) -> {
            SyncTimerData data = read(buf);

            boolean bl = data.shouldTick;
            double syncedTime = data.time;

            client.execute(() -> {
                DDVGamesClient.TIMER_ENABLED = bl;
                DDVGamesClient.TIMER = syncedTime;
            });
        };
    }

    @Override
    public PacketByteBuf write(SyncTimerData data) {
        PacketByteBuf buf = getNewBuf();

        buf.writeBoolean(data.shouldTick);
        buf.writeDouble(data.time);

        return buf;
    }

    @Override
    public SyncTimerData read(PacketByteBuf buf) {
        SyncTimerData data = new SyncTimerData();

        data.shouldTick = buf.readBoolean();
        data.time = buf.readDouble();

        return data;
    }

    public static class SyncTimerData extends NervePacketData {
        public boolean shouldTick;
        public double time;
    }
}
