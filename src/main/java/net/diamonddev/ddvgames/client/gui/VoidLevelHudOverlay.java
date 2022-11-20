package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VoidLevelHudOverlay implements HudRenderCallback {

    private static final Identifier ISLAND_TEXTURE = DDVGamesMod.id.build("textures/ui/rising_edge/island.png");
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        int x = 0;
        int y = 0;
        int width, height;
        MinecraftClient client = MinecraftClient.getInstance();
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
        RenderSystem.setShaderTexture(0, ISLAND_TEXTURE);

        // render
        DrawableHelper.drawTexture(matrixStack, x, y, 0, 0, 32, 32, 16, 16); // texture
        if (client != null) {
            DrawableHelper.drawTextWithShadow( // text
                    matrixStack, client.textRenderer,
                    Text.literal(Double.toString(DDVGamesMod.gameManager.getTimer())),
                    x - 100, y - 40,
                    0xf // white color in hexadecimal
            );
        }
    }
}
