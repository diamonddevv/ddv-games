package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.math.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LivesHudOverlay implements IHudRenderer {

    private static final Identifier HEART_TEXTURE = DDVGamesMod.id.build("textures/ui/rising_edge/lives.png");
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
            RenderSystem.setShaderTexture(0, HEART_TEXTURE);

            // render
            DrawableHelper.drawTexture(matrixStack, (x * 2) - 40, getTextureBindY(1), 0, 0, 16, 16, 16, 16); // texture
            DrawableHelper.drawTextWithShadow( // text
                    matrixStack, textRenderer,
                    Text.literal("" + DDVGamesEntityComponents.getLives(client.player)),
                    (x * 2) - 20, getTextBindY(1),
                    0xffffff // white color in hexadecimal
            );
        }
    }
}
