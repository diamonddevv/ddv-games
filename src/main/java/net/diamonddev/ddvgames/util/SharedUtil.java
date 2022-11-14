package net.diamonddev.ddvgames.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class SharedUtil {
    public static void changePlayerGamemode(PlayerEntity player, GameMode gameMode) {
        try {
            MinecraftServer server = player.getServer();
            ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());

            serverPlayer.changeGameMode(gameMode);
        } catch (Exception ignored) {}
    }

    public static <T extends ParticleEffect> void spawnParticle(ServerWorld serverWorld, T particle, Vec3d pos, Vec3d deltaPos, int count, double speed) {
        serverWorld.spawnParticles(particle, pos.x, pos.y, pos.z, count, deltaPos.x, deltaPos.y, deltaPos.z, speed);
    }

    public static Vec3d cubeVec(double d) {
        return new Vec3d(d, d ,d);
    }
}
