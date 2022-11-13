package net.diamonddev.ddvgames.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class PlayerUtil {
    public static void changePlayerGamemode(PlayerEntity player, GameMode gameMode) {
        try {
            MinecraftServer server = player.getServer();
            ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());

            serverPlayer.changeGameMode(gameMode);
        } catch (Exception ignored) {}
    }
}
