package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.network.*;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NervePacketRegistry;

public class InitPackets implements RegistryInitializer {

    public static NervePacketRegistry.NervePacketRegistryEntry<SyncGameS2CPacket, SyncGameS2CPacket.SyncGamePacketData> SYNC_GAME;
    public static NervePacketRegistry.NervePacketRegistryEntry<SyncGameStateS2CPacket, SyncGameStateS2CPacket.SyncGameStateData> SYNC_STATE;
    public static NervePacketRegistry.NervePacketRegistryEntry<SyncLivesS2CPacket, SyncLivesS2CPacket.SyncLivesData> SYNC_LIVES;
    public static NervePacketRegistry.NervePacketRegistryEntry<SyncPlayersS2CPacket, SyncPlayersS2CPacket.SyncPlayersData> SYNC_PLAYERS;
    public static NervePacketRegistry.NervePacketRegistryEntry<SyncRoleS2CPacket, SyncRoleS2CPacket.SyncRoleData> SYNC_ROLE;
    public static NervePacketRegistry.NervePacketRegistryEntry<SyncTimerS2CPacket, SyncTimerS2CPacket.SyncTimerData> SYNC_TIMER;

    public static NervePacketRegistry.NervePacketRegistryEntry<SyncVoidLevelS2CPacket, SyncVoidLevelS2CPacket.SyncVoidLevelData> SYNC_VOIDLEVEL;


    @Override
    public void register() {
        SYNC_GAME = NervePacketRegistry.register(DDVGamesMod.id("sync_game_packet"), new SyncGameS2CPacket());
        SYNC_STATE = NervePacketRegistry.register(DDVGamesMod.id("sync_state_packet"), new SyncGameStateS2CPacket());
        SYNC_LIVES = NervePacketRegistry.register(DDVGamesMod.id("sync_lives_packet"), new SyncLivesS2CPacket());
        SYNC_PLAYERS = NervePacketRegistry.register(DDVGamesMod.id("sync_players_packet"), new SyncPlayersS2CPacket());
        SYNC_ROLE = NervePacketRegistry.register(DDVGamesMod.id("sync_role_packet"), new SyncRoleS2CPacket());
        SYNC_TIMER = NervePacketRegistry.register(DDVGamesMod.id("sync_timer_packet"), new SyncTimerS2CPacket());
        SYNC_VOIDLEVEL = NervePacketRegistry.register(DDVGamesMod.id("sync_voidlevel_packet"), new SyncVoidLevelS2CPacket());
    }
}
