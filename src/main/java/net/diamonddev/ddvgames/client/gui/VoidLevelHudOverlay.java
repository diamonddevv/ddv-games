package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.ddvgames.minigame.RisingEdgeMinigame;
import net.diamonddev.ddvgames.registry.InitMinigames;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VoidLevelHudOverlay implements IHudRenderer {

    private static final Identifier ISLAND_TEXTURE = DDVGamesMod.id("textures/ui/rising_edge/island.png");
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta, MinecraftClient client, TextRenderer textRenderer) {
        if (DDVGamesClient.hasGameAndRunning(InitMinigames.RISING_EDGE)) {
            int x, y;
            int width, height;

            // Get Width, Height, X and Y
            width = client.getWindow().getScaledWidth();
            height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;

            // initialize rendering for this
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            // Set Shader Texture to Stopwatch
            RenderSystem.setShaderTexture(0, ISLAND_TEXTURE);

            // render
            DrawableHelper.drawTexture(matrixStack, getTextureBindX(BindingSide.RIGHT, x), getTextureBindY(1), 0, 0, 16, 16, 16, 16); // texture
            DrawableHelper.drawTextWithShadow( // text
                    matrixStack, textRenderer,
                    Text.literal("" + DDVGamesClient.VOID_LEVEL),
                    getTextBindX(BindingSide.RIGHT, x), getTextBindY(1),
                    0xffffff // white color in hexadecimal
            );
        }
    }
}
