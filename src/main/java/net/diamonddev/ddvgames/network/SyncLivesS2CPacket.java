package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SyncLivesS2CPacket implements NerveS2CPacket<SyncLivesS2CPacket, SyncLivesS2CPacket.SyncLivesData> {

    @Override
    public ClientPlayNetworking.PlayChannelHandler receive(Identifier channel) {
        return (client, handler, buf, responseSender) -> {
            // Get Packet Data
            SyncLivesData data = read(buf);

            int lives = data.lives;
            PlayerEntity player = handler.getWorld().getPlayerByUuid(data.playerUUID);


            client.execute(() -> DDVGamesClient.HASHED_LIVES.put(player, lives));
        };
    }

    @Override
    public PacketByteBuf write(SyncLivesData data) {
        PacketByteBuf buf = getNewBuf();

        buf.writeInt(data.lives);
        buf.writeUuid(data.playerUUID);

        return buf;
    }

    @Override
    public SyncLivesData read(PacketByteBuf buf) {
        SyncLivesData data = new SyncLivesData();

        data.lives = buf.readInt();
        data.playerUUID = buf.readUuid();

        return data;
    }

    public static class SyncLivesData extends NervePacketData {
        public int lives;
        public UUID playerUUID;
    }
}
