package net.diamonddev.ddvgames.mixin;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.minigame.RisingEdgeMinigame;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.registry.InitMinigames;
import net.diamonddev.ddvgames.registry.InitRules;
import net.diamonddev.ddvgames.util.SharedUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void ddvg$disallowFriendlyFire(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!world.getGameRules().getBoolean(InitRules.PVP)) {
            if (source.getSource() instanceof PlayerEntity) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void ddvg$risingEdge$onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (DDVGamesMod.gameManager.isGameRunning(InitMinigames.RISING_EDGE)) {
            // kill effects and set to spectator after final death
            SharedUtil.spawnParticle(Objects.requireNonNull(Objects.requireNonNull(this.world.getServer()).getWorld(this.world.getRegistryKey())),
                    ParticleTypes.ELECTRIC_SPARK, this.getPos(), SharedUtil.cubeVec(0.22), 50, 0.1);

            if (DDVGamesEntityComponents.getLives((PlayerEntity) (Object)this) <= 0) {
                DDVGamesMod.gameManager.attachRole((PlayerEntity) (Object) this, Role.fromName(RisingEdgeMinigame.SPECTATOR));
            }
        }
    }
}
