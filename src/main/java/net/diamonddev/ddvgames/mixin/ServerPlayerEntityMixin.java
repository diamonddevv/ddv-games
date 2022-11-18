package net.diamonddev.ddvgames.mixin;

import com.mojang.authlib.GameProfile;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.minigame.RisingEdgeMinigame;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.registry.InitMinigames;
import net.diamonddev.ddvgames.util.SharedUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void ddvg$risingEdge$onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (DDVGamesMod.gameManager.isGameRunning(InitMinigames.RISING_EDGE)) {
            // kill effects and set to spectator after final death


            SharedUtil.spawnParticle(Objects.requireNonNull(Objects.requireNonNull(this.world.getServer()).getWorld(this.world.getRegistryKey())),
                    ParticleTypes.ELECTRIC_SPARK, this.getPos(), SharedUtil.cubeVec(0.22), 50, 0.1);



            DDVGamesEntityComponents.setLives(this, DDVGamesEntityComponents.getLives(this) - 1);
            if (DDVGamesEntityComponents.getLives(this) <= 0) {
                DDVGamesMod.gameManager.attachRole(this, Role.fromName(RisingEdgeMinigame.SPECTATOR));
                SharedUtil.changePlayerGamemode(this, GameMode.SPECTATOR);
            }

        }
    }
}
