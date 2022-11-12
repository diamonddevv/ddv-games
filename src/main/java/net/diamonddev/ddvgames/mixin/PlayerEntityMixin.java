package net.diamonddev.ddvgames.mixin;

import net.diamonddev.ddvgames.registry.InitRules;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
}
