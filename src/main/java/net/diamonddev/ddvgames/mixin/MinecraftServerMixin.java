package net.diamonddev.ddvgames.mixin;

import dev.onyxstudios.cca.api.v3.level.LevelComponents;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesLevelComponents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "loadWorld", at = @At("HEAD"))
    public void ddvg$loadAndSyncLevelCardinalComponents(CallbackInfo ci) {
        LevelComponents.sync(DDVGamesLevelComponents.GAME_MANAGER, (MinecraftServer) (Object) this);
        DDVGamesMod.LOGGER.info("Synced & Loaded Game Manager Data Successfully!");
    }
    @Inject(method = "shutdown", at = @At("HEAD"))
    public void ddvg$saveAndSyncLevelCardinalComponents(CallbackInfo ci) {
        LevelComponents.sync(DDVGamesLevelComponents.GAME_MANAGER, (MinecraftServer) (Object) this);
        DDVGamesMod.LOGGER.info("Synced & Saved Game Manager Data Successfully!");
    }
}
