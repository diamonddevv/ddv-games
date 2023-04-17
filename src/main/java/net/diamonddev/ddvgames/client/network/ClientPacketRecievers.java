package net.diamonddev.ddvgames.client.network;


import net.diamonddev.ddvgames.registry.InitPackets;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NervePacketRegistry;

public class ClientPacketRecievers implements RegistryInitializer {
    @Override
    public void register() {
        // Nerve
        NervePacketRegistry.initClientS2CReciever(InitPackets.SYNC_GAME);
        NervePacketRegistry.initClientS2CReciever(InitPackets.SYNC_STATE);
        NervePacketRegistry.initClientS2CReciever(InitPackets.SYNC_LIVES);
        NervePacketRegistry.initClientS2CReciever(InitPackets.SYNC_PLAYERS);
        NervePacketRegistry.initClientS2CReciever(InitPackets.SYNC_ROLE);
        NervePacketRegistry.initClientS2CReciever(InitPackets.SYNC_TIMER);
        NervePacketRegistry.initClientS2CReciever(InitPackets.SYNC_LIVES);
    }
}
