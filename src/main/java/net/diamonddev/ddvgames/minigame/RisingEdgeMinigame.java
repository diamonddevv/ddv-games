package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.util.SharedUtil;
import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static net.diamonddev.ddvgames.minigame.Setting.*;

public class RisingEdgeMinigame extends Minigame {

    private double previousBorderSize = 0.0;
    private Vec2f previousCenter = new Vec2f(0.0f, 0.0f);
    private GameRules previousRules;

    private static final StatusEffectInstance INF_GLOWING_EFFECT_INSTANCE =
            new StatusEffectInstance(StatusEffects.GLOWING, (int) Double.POSITIVE_INFINITY, 0, true, false, false);
    public RisingEdgeMinigame() {
        super(Text.translatable("ddv.minigame.rising_edge"), "0.0.1", SemanticVersioningSuffix.TEST);
    }

    @Override
    public ArrayList<Role> addRoles(ArrayList<Role> roles) {
        roles.add(new Role("spectator"));
        roles.add(new Role("player"));
        return roles;
    }

    @Override
    public ArrayList<Setting> addSettings(ArrayList<Setting> settings) {
        settings.add(new Setting(3.0, "livesPerPlayer"));
        settings.add(new Setting(0.0, "giveGlowing"));
        settings.add(new Setting(1.0, "allowHealing"));
        settings.add(new Setting(100.0, "borderDistance"));
        settings.add(new Setting(600.0, "warmupSeconds"));
        settings.add(new Setting(3.0, "riseInterval"));
        return settings;
    }

    @Override
    public void onStart(Entity executor, Collection<PlayerEntity> players, World world) {
        double borderDist = parseAsDouble("borderDistance");
        int warmupTicks = parseAsInt("warmupSeconds") * 20;
        boolean glowing = parseAsBoolean("giveGlowing");

        Collection<PlayerEntity> roledPlayers = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName("player"));
        Collection<PlayerEntity> roledSpectators = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName("spectator"));

        this.previousBorderSize = world.getWorldBorder().getSize();
        this.previousCenter = new Vec2f((float)world.getWorldBorder().getCenterX(), (float)world.getWorldBorder().getCenterZ());
        this.previousRules = world.getGameRules().copy();

        world.getWorldBorder().setCenter(executor.getX(), executor.getZ());
        world.getWorldBorder().setSize(borderDist);

        // Spectators in Spectator Mode
        roledSpectators.forEach(player -> SharedUtil.changePlayerGamemode(player, GameMode.SPECTATOR));

        if (glowing) { // Glowing to players, if enabled
            roledPlayers.forEach(player -> player.addStatusEffect(INF_GLOWING_EFFECT_INSTANCE));
        }

        // Clear all players inventories
        roledPlayers.forEach(player -> player.getInventory().clear());
    }

    @Override
    public void onEnd(Collection<PlayerEntity> players, World world) {
        world.getWorldBorder().setCenter(this.previousCenter.x, this.previousCenter.y);
        world.getWorldBorder().setSize(this.previousBorderSize);
        world.getGameRules().setAllValues(this.previousRules, world.getServer());
    }

    @Override
    public boolean canWin(PlayerEntity winnerCandidate, Collection<PlayerEntity> players) {
        players.removeIf(player -> DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName("spectator")).contains(player));
        players.remove(winnerCandidate);
        return players.stream().allMatch(PlayerEntity::isDead);
    }

    @Override
    public void onWin(PlayerEntity winningPlayer, World world, Collection<PlayerEntity> players) {
        players.forEach(player -> {
            player.sendMessage(Text.translatable("ddv.minigame.rising_edge.win_title", winningPlayer.getGameProfile().getName()), true);
        });

        ServerWorld serverWorld = null;
        try { serverWorld = Objects.requireNonNull(world.getServer()).getWorld(winningPlayer.getWorld().getRegistryKey());
        } catch (Exception ignored) {}

        if (serverWorld != null) {
            SharedUtil.spawnParticle(serverWorld, ParticleTypes.TOTEM_OF_UNDYING, winningPlayer.getPos(), SharedUtil.cubeVec(0.5), 5000, 0.5);
        }

        players.forEach(player -> player.getInventory().clear());
    }

    @Override
    public void tickClock() { // TICK CLOCK
        System.out.println("Tick: " + this.getTicks());
    }

    @Override
    public boolean canStart(Collection<PlayerEntity> players) {
        return DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName("player")).size() > 1;
    }
}
