package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.math.MathUtil;
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
        if (DDVGamesMod.gameManager.getGameHasStarted()) {
            int x, y;
            int width, height;

            // Get Width, Height, X and Y
            width = client.getWindow().getScaledWidth();
            height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;

            // initialize rendering for this
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            // Set Shader Texture to Stopwatch
            RenderSystem.setShaderTexture(0, STOPWATCH_TEXTURE);

            // render - - todo: adjust positions into place
            DrawableHelper.drawTexture(matrixStack, x - 180, y- 180, 0, 0, 16, 16, 16, 16); // texture
            DrawableHelper.drawTextWithShadow( // text
                    matrixStack, textRenderer,
                    Text.literal("" + MathUtil.round(DDVGamesMod.gameManager.getTimer(), 0)), // This is inaccurate for some reason
                    x + 5, y - 200,
                    0xffffff // white color in hexadecimal
            );
        }
    }
}
