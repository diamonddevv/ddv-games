package net.diamonddev.ddvgames.client.gui;

import net.diamonddev.ddvgames.math.SimpleVec2i;
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

    default int getTextureBindX(BindingSide side, int halfScaledWidth) {
        return side.equals(BindingSide.RIGHT) ? (halfScaledWidth * 2) - 45 : 20;
    }
    default int getTextBindX(BindingSide side, int halfScaledWidth) {
        return side.equals(BindingSide.RIGHT) ? (halfScaledWidth * 2) - 25 : 40;
    }


    default SimpleVec2i getIconBinding(int ordinal, int height) {
        return new SimpleVec2i(20 * (ordinal + 1), height - 26);
    }

    enum BindingSide {
        LEFT,
        RIGHT
    }
}
