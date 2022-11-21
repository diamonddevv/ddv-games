package net.diamonddev.ddvgames.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface IHudRenderer {
    void onHudRender(MatrixStack matrixStack, float tickDelta, MinecraftClient client, TextRenderer textRenderer);
}
