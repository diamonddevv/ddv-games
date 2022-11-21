package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GameTimeHudOverlay implements IHudRenderer {

    private static final Identifier STOPWATCH_TEXTURE = DDVGamesMod.id.build("textures/ui/common/stopwatch.png");
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta, MinecraftClient client, TextRenderer textRenderer) {
        int x = 0;
        int y = 0;
        int width, height;

        // Get Width, Height, X and Y
        if (client != null) {
            width = client.getWindow().getWidth();
            height = client.getWindow().getHeight();

            x = width / 2;
            y = height;
        }

        // initialize rendering for this
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        // Set Shader Texture to Stopwatch
        RenderSystem.setShaderTexture(0, STOPWATCH_TEXTURE);

        // render
        DrawableHelper.drawTexture(matrixStack, x - 150, y - 24, 0, 0, 64, 64, 16, 16); // texture
        DrawableHelper.drawTextWithShadow( // text
                matrixStack, textRenderer,
                Text.literal(Double.toString(DDVGamesMod.gameManager.getTimer())),
                x + 120, y - 24,
                0xf // white color in hexadecimal
        );
    }
}
