package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.minigame.setting.Setting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class RisingEdgeMinigame extends Minigame {
    public RisingEdgeMinigame() {
        super(Text.translatable("ddv.minigame.rising_edge"));
    }

    @Override
    public ArrayList<Setting> addSettings(ArrayList<Setting> settings) {
        settings.add(new Setting(3.0, "livesPerPlayer"));
        settings.add(new Setting(1.0, "useSpawnPlatform"));
        settings.add(new Setting(0.0, "giveGlowing"));
        settings.add(new Setting(1.0, "allowHealing"));
        return settings;
    }

    @Override public boolean canStart(Collection<PlayerEntity> players) {
        return true;
    }
}
