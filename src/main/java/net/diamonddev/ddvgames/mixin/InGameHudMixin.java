package net.diamonddev.ddvgames.mixin;

import net.diamonddev.ddvgames.client.gui.*;
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

    // Common
    private final IHudRenderer time = new GameTimeHudOverlay();

    // Rising Edge
    private final IHudRenderer lives = new LivesHudOverlay();
    private final IHudRenderer voidLevel = new VoidLevelHudOverlay();
    private final IHudRenderer playerCount = new PlayerCountHudOverlay();

    private final IHudRenderer roleIcon = new RisingEdgeRoleIconHudRenderer();
    private final IHudRenderer phaseIcon = new RisingEdgeStateIconHudRenderer();

    @Inject(method = "render", at = @At("HEAD"))
    private void ddvg$drawHudElements(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!client.options.hudHidden) {
            time.onHudRender(matrices, tickDelta, client, this.getTextRenderer());

            lives.onHudRender(matrices, tickDelta, client, this.getTextRenderer());
            voidLevel.onHudRender(matrices, tickDelta, client, this.getTextRenderer());
            playerCount.onHudRender(matrices, tickDelta, client, this.getTextRenderer());
            roleIcon.onHudRender(matrices, tickDelta, client, this.getTextRenderer());
            phaseIcon.onHudRender(matrices, tickDelta, client, this.getTextRenderer());
        }
    }

}
