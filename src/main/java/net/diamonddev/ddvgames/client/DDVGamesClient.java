package net.diamonddev.ddvgames.client;

import net.fabricmc.api.ClientModInitializer;

public class DDVGamesClient implements ClientModInitializer {

    public static double CLIENT_SYNCED_GAMETIME;
    public static double CLIENT_SYNCED_VOID_LEVEL;

    @Override
    public void onInitializeClient() {
        // register gametime hud overlay
//        HudRenderCallback.EVENT.register(new GameTimeHudOverlay());
//        HudRenderCallback.EVENT.register(new VoidLevelHudOverlay());
    }
}
