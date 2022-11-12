package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

import static net.diamonddev.ddvgames.minigame.Setting.parseAsBoolean;
import static net.diamonddev.ddvgames.minigame.Setting.parseAsDouble;

public class RisingEdgeMinigame extends Minigame {

    private double previousBorderSize = 0.0;
    private Vec2f previousCenter = new Vec2f(0.0f, 0.0f);
    private GameRules previousRules;

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
        settings.add(new Setting(1.0, "useSpawnPlatform"));
        settings.add(new Setting(0.0, "giveGlowing"));
        settings.add(new Setting(1.0, "allowHealing"));
        settings.add(new Setting(100.0, "borderDistance"));
        settings.add(new Setting(600.0, "warmupSeconds"));
        return settings;
    }

    @Override
    public void onStart(Entity executor, Collection<PlayerEntity> players, World world) {
        double borderDist = parseAsDouble("borderDistance");
        boolean useSpawnPlatform = parseAsBoolean("useSpawnPlatform");
        int warmupTicks = (int) parseAsDouble("warmupSeconds") * 20;

        this.previousBorderSize = world.getWorldBorder().getSize();
        this.previousCenter = new Vec2f((float)world.getWorldBorder().getCenterX(), (float)world.getWorldBorder().getCenterZ());
        this.previousRules = world.getGameRules().copy();

        world.getWorldBorder().setCenter(executor.getX(), executor.getZ());
        world.getWorldBorder().setSize(borderDist);

        DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName("spectator")).forEach(player -> player.set);
    }

    @Override
    public void onEnd(Collection<PlayerEntity> players, World world) {
        world.getWorldBorder().setCenter(this.previousCenter.x, this.previousCenter.y);
        world.getWorldBorder().setSize(this.previousBorderSize);
        world.getGameRules().setAllValues(this.previousRules, world.getServer());
    }

    @Override
    public boolean canStart(Collection<PlayerEntity> players) {
        return true;
    }
}
