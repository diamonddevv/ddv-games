package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.RisingEdgeMinigame;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.registry.InitMinigames;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PlayerCountHudOverlay implements IHudRenderer {
    private static final Identifier PLAYERS_TEXTURE = DDVGamesMod.id.build("textures/ui/rising_edge/players.png");
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta, MinecraftClient client, TextRenderer textRenderer) {
        if (DDVGamesMod.gameManager.getSpecificGameHasStarted(InitMinigames.RISING_EDGE) ) {
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
            RenderSystem.setShaderTexture(0, PLAYERS_TEXTURE);

            // render
            DrawableHelper.drawTexture(matrixStack, getTextureBindX(BindingSide.LEFT, x), getTextureBindY(1), 0, 0, 16, 16, 16, 16); // texture
            DrawableHelper.drawTextWithShadow( // text
                    matrixStack, textRenderer,
                    Text.literal("" + DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(RisingEdgeMinigame.PLAYER)).size()),
                    getTextBindX(BindingSide.LEFT, x), getTextBindY(1),
                    0xffffff // white color in hexadecimal
            );
        }
    }
}
