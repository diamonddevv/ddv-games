package net.diamonddev.ddvgames.minigame;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;

import java.util.Collection;

public class TestMinigame extends Minigame {
    public TestMinigame() {
        super(Text.translatable("ddv.minigame.test"));
    }
    @Override
    public boolean canStart(Collection<PlayerEntity> players) {
        return true;
    }

    @Override
    public PlayerEntity checkWin(Collection<PlayerEntity> players) {
        for (PlayerEntity player : players) {
            if (player.isSneaking()) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void onPlayerWin(PlayerEntity player) {
        for (int i = 0; i < 10; i++) {
            player.getWorld().addParticle(
                    ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY(), player.getZ(),
                    1.0, 1.0, 1.0
            );
        }
    }

    @Override
    public void onRunClockCycle(Collection<PlayerEntity> players) {
        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 60));
        }
    }
}
