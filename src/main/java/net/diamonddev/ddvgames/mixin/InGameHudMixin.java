package net.diamonddev.ddvgames.mixin;

import net.diamonddev.ddvgames.client.gui.GameTimeHudOverlay;
import net.diamonddev.ddvgames.client.gui.IHudRenderer;
import net.diamonddev.ddvgames.client.gui.LivesHudOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    private final IHudRenderer timeHudRenderer = new GameTimeHudOverlay();
    private final IHudRenderer livesHudRenderer = new LivesHudOverlay();

    @Inject(method = "render", at = @At("HEAD"))
    private void ddvg$drawHudElements(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!client.options.hudHidden) {
            timeHudRenderer.onHudRender(matrices, tickDelta, client, this.getTextRenderer());
            livesHudRenderer.onHudRender(matrices, tickDelta, client, this.getTextRenderer());
        }
    }

}
