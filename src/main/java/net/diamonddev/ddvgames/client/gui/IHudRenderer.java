package net.diamonddev.ddvgames.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface IHudRenderer {
    void onHudRender(MatrixStack matrixStack, float tickDelta, MinecraftClient client, TextRenderer textRenderer);

    default int getTextureBindY(int ordinal) {
       int initialGap = 10;
       int gap = 15;

       return initialGap + (16 * (ordinal + 1)) + (ordinal * gap);
    }

    default int getTextBindY(int ordinal) {
        int initialGap = 15;
        int gap = 15;

        return initialGap + (16 * (ordinal + 1)) + (ordinal * gap);
    }
}
