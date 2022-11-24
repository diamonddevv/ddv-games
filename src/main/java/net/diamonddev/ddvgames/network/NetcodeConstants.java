package net.diamonddev.ddvgames.network;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.minecraft.util.Identifier;

public class NetcodeConstants {
    public static Identifier SYNC_LIVES_ID = DDVGamesMod.id.build("sync_lives_packet");
    public static Identifier SYNC_ROLES_ID = DDVGamesMod.id.build("sync_roles_packet");
}
