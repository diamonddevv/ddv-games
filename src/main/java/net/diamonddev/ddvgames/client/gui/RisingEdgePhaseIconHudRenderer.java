package net.diamonddev.ddvgames.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.client.DDVGamesClient;
import net.diamonddev.ddvgames.math.SimpleVec2i;
import net.diamonddev.ddvgames.registry.InitMinigames;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RisingEdgePhaseIconHudRenderer implements IHudRenderer {

    private static final Identifier WARMUP_ICON = DDVGamesMod.id.build("textures/ui/rising_edge/phase/warmup.png");
    private static final Identifier PVP_ICON = DDVGamesMod.id.build("textures/ui/rising_edge/phase/pvp.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta, MinecraftClient client, TextRenderer textRenderer) {
        if (DDVGamesMod.gameManager.getSpecificGameHasStarted(InitMinigames.RISING_EDGE)) {

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

            // Set Shader Texture to correct Icon
            Identifier texture = switch (DDVGamesMod.gameManager.getCurrentState().getName()) {
                case "warmup" -> WARMUP_ICON;
                case "pvp" -> PVP_ICON;
                default -> throw new IllegalStateException("Unexpected value: " + DDVGamesClient.HASHED_ROLES.get(client.player).getName());
            };

            RenderSystem.setShaderTexture(0, texture);

            // render
            SimpleVec2i vec = getIconBinding(1, y);
            DrawableHelper.drawTexture(
                    matrixStack,
                    vec.getX(), vec.getY(),
                    0, 0,
                    16, 16,
                    16, 16);
        }
    }
}
