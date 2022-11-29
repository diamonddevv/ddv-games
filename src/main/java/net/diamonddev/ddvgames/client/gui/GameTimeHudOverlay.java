package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.ddvgames.math.MathUtil;
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
        if (DDVGamesClient.IS_GAME_STARTED) {
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

            // render
            DrawableHelper.drawTexture(matrixStack, getTextureBindX(BindingSide.LEFT, x), getTextureBindY(0), 0, 0, 16, 16, 16, 16); // texture
            DrawableHelper.drawTextWithShadow( // text
                    matrixStack, textRenderer,
                    Text.literal("" + MathUtil.round(DDVGamesMod.gameManager.getTimer(), 1)), // This is inaccurate for some reason
                    getTextBindX(BindingSide.LEFT, x), getTextBindY(0),
                    0xffffff // white color in hexadecimal
            );
        }
    }
}
