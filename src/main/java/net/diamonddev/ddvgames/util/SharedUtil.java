package net.diamonddev.ddvgames.util;

import net.diamonddev.ddvgames.math.Quadrilateral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
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

    public static Quadrilateral createQuad(double radius, Vec2f center) {
        return new Quadrilateral(center.add((float)radius * 2), center);
    }

    public static Vec2f vec2fPow(Vec2f input, float raise) {
        return new Vec2f((float) Math.pow(input.x, raise), (float) Math.pow(input.y, raise));
    }
    public static Vec3d xzVec2fToVec3d(Vec2f vec, float y) {
        return new Vec3d(vec.x, y, vec.y);
    }
}
