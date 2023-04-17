package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SyncGameS2CPacket implements NerveS2CPacket<SyncGameS2CPacket, SyncGameS2CPacket.SyncGamePacketData> {

    @Override
    public ClientPlayNetworking.PlayChannelHandler receive(Identifier channel) {
        return (client, handler, buf, responseSender) -> {
            SyncGamePacketData data = read(buf);

            Minigame game = InitRegistries.MINIGAMES.get(data.gameId);
            boolean gameRunning = data.isRunning;

            client.execute(() -> {
                DDVGamesClient.CURRENT_GAME = game;
                DDVGamesClient.IS_GAME_STARTED = gameRunning;
            });
        };
    }

    @Override
    public PacketByteBuf write(SyncGamePacketData data) {
        PacketByteBuf buf = getNewBuf();

        buf.writeIdentifier(data.gameId);
        buf.writeBoolean(data.isRunning);

        return buf;
    }

    @Override
    public SyncGamePacketData read(PacketByteBuf buf) {
        SyncGamePacketData data = new SyncGamePacketData();

        data.gameId = buf.readIdentifier();
        data.isRunning = buf.readBoolean();

        return data;
    }

    public static class SyncGamePacketData extends NervePacketData {
        public Identifier gameId;
        public boolean isRunning;
    }
}
