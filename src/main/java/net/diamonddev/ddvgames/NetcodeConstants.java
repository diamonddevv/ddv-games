package net.diamonddev.ddvgames;

import net.minecraft.util.Identifier;

public class NetcodeConstants {
    public static Identifier SYNC_GAME = DDVGamesMod.id.build("sync_game_packet");
    public static Identifier SYNC_PLAYERCOUNT = DDVGamesMod.id.build("sync_playercount_packet");
    public static Identifier SYNC_STATE = DDVGamesMod.id.build("sync_gamestate_packet");

    public static Identifier SYNC_LIVES_ID = DDVGamesMod.id.build("sync_lives_packet");
    public static Identifier SYNC_ROLES_ID = DDVGamesMod.id.build("sync_roles_packet");
}
