package net.diamonddev.ddvgames.mixin;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract ServerWorld getOverworld();

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void ddvg$stopMinigameIfRunning(CallbackInfo ci) {
        if (DDVGamesMod.gameManager.isGameRunning()) {
            DDVGamesMod.LOGGER.info("Stopping running minigame..");
            DDVGamesMod.gameManager.stopGame(this.getOverworld());
            DDVGamesMod.LOGGER.info("Stopped running minigame!");
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void ddvg$runMinigameTickClock(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (DDVGamesMod.gameManager.isGameRunning()) {
            Minigame minigame = DDVGamesMod.gameManager.getGame();
            DDVGamesMod.gameManager.tick();

            minigame.tryWin(this.getOverworld());
            minigame.changeTickClock();

            minigame.tickClock(this.getOverworld());
            minigame.essentialTicks(this.getOverworld());
        }
    }
}
