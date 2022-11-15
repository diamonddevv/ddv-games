package net.diamonddev.ddvgames.mixin;

import dev.onyxstudios.cca.api.v3.level.LevelComponents;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesLevelComponents;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.registry.InitRegistries;
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

    @Inject(method = "loadWorld", at = @At("HEAD"))
    public void ddvg$loadAndSyncLevelCardinalComponents(CallbackInfo ci) {
        LevelComponents.sync(DDVGamesLevelComponents.GAME_MANAGER, (MinecraftServer) (Object) this);
        DDVGamesMod.LOGGER.info("Synced & Loaded Game Manager Data Successfully! -- Note: This is actually a lie, this doesnt work yet lol");
    }
    @Inject(method = "shutdown", at = @At("HEAD"))
    public void ddvg$saveAndSyncLevelCardinalComponents(CallbackInfo ci) {
        LevelComponents.sync(DDVGamesLevelComponents.GAME_MANAGER, (MinecraftServer) (Object) this);
        DDVGamesMod.LOGGER.info("Synced & Saved Game Manager Data Successfully! -- Note: This is actually a lie, this doesnt work yet lol");
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void ddvg$runMinigameTickClock(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (DDVGamesMod.gameManager.isGameRunning()) {
            Minigame minigame = DDVGamesMod.gameManager.getGame();
            DDVGamesMod.gameManager.tick();
            minigame.tryWin();
            minigame.tickClock(this.getOverworld());
            minigame.changeTickClock();
        }
    }
}
