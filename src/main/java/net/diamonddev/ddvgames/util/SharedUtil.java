package net.diamonddev.ddvgames.util;

import net.diamonddev.ddvgames.math.Cube;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;


public class SharedUtil {
    public static void changePlayerGamemode(PlayerEntity player, GameMode gameMode) {
        try {
            MinecraftServer server = player.getServer();
            ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());

            serverPlayer.changeGameMode(gameMode);
        } catch (Exception ignored) {}
    }

    public static void pushPlayerTitle(PlayerEntity player, Text title) {
        try {
            ServerPlayerEntity serverPlayer = player.getServer().getPlayerManager().getPlayer(player.getUuid());
            serverPlayer.networkHandler.sendPacket(new TitleS2CPacket(title));
        } catch (Exception ignored) {}
    }

    public static void pushPlayerSubtitle(PlayerEntity player, Text subtitle) {
        try {
            ServerPlayerEntity serverPlayer = player.getServer().getPlayerManager().getPlayer(player.getUuid());
            serverPlayer.networkHandler.sendPacket(new SubtitleS2CPacket(subtitle));
        } catch (Exception ignored) {}
    }

    public static <T extends ParticleEffect> void spawnParticle(ServerWorld serverWorld, T particle, Vec3d pos, Vec3d deltaPos, int count, double speed) {
        serverWorld.spawnParticles(particle, pos.x, pos.y, pos.z, count, deltaPos.x, deltaPos.y, deltaPos.z, speed);
    }

    public static <T extends ParticleEffect> void spawnParticle(ServerWorld serverWorld, T particle, double yOffset, Vec3d pos, Vec3d deltaPos, int count, double speed) {
        serverWorld.spawnParticles(particle, pos.x, pos.y + yOffset, pos.z, count, deltaPos.x, deltaPos.y, deltaPos.z, speed);
    }

    public static Vec3d cubeVec(double d) {
        return new Vec3d(d, d ,d);
    }

    public static Cube createCube(double radius, Vec2f center, double height, double base) {
        Vec2f cornerAdd = center.add((float) (radius));
        Vec2f cornerSubtract = center.add((float) -(radius));
        Vec3d bottomA = new Vec3d(cornerAdd.x, base, cornerAdd.y);
        Vec3d topB = new Vec3d(cornerSubtract.x, height, cornerSubtract.y);
        return new Cube(bottomA, topB);
    }

    public static double getSquareHypotenuse(double length) { // might use. could make above method use this for funsies or something idk
        return Math.sqrt(Math.pow(length, 2.0) * 2.0);
    }

    public static Vec3d addY(Vec2f vec2f, double y) {
        return new Vec3d(vec2f.x, y, vec2f.y);
    }

    public static Vec3d blockPosToVec(BlockPos blockPos) {
        return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static BlockPos vecToBlockPos(Vec3d vec) {
        return new BlockPos(vec.x, vec.y, vec.z);
    }

    public static ServerPlayerEntity getServerPlayer(PlayerEntity player, ServerWorld world) {
        return world.getServer().getPlayerManager().getPlayer(player.getUuid());
    }
}
