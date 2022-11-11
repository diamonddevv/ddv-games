package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

import static net.diamonddev.ddvgames.minigame.Setting.parseAsBoolean;
import static net.diamonddev.ddvgames.minigame.Setting.parseAsDouble;

public class RisingEdgeMinigame extends Minigame {
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
        return settings;
    }

    @Override
    public void onStart() {
        double borderDist = parseAsDouble("borderDistance");
        boolean useSpawnPlatform = parseAsBoolean("useSpawnPlatform");


    }

    @Override
    public void onEnd() {

    }

    @Override public boolean canStart(Collection<PlayerEntity> players) {
        return players.size() >= 2;
    }
}
