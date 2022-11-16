package net.diamonddev.ddvgames.util;

import net.diamonddev.ddvgames.math.Quadrilateral;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
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
        double sqrhyp = getSquareHyp(radius);
        Vec2f cornerA = center.add((float) sqrhyp);
        Vec2f cornerB = center.add((float) -sqrhyp);
        return new Quadrilateral(cornerA, cornerB);
    }

    public static Box getBoxFromQuad(Quadrilateral quad, double base, double height) {
        return new Box(xzVec2fToVec3d(quad.getCornerA(), (float)base), xzVec2fToVec3d(quad.getCornerB(), (float)height));
    }

    public static Vec2f vec2fPow(Vec2f input, float raise) {
        return new Vec2f((float) Math.pow(input.x, raise), (float) Math.pow(input.y, raise));
    }
    public static Vec3d xzVec2fToVec3d(Vec2f vec, float y) {
        return new Vec3d(vec.x, y, vec.y);
    }

    public static double getSquareHyp(double length) {
        return Math.sqrt((Math.pow(length, 2.0))*2);
    }
}
