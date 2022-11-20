package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class SyncGameTimePacketS2C {
    public static void recieve(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender response) {
        DDVGamesClient.CLIENT_SYNCED_GAMETIME = buf.readDouble();
    }

}
