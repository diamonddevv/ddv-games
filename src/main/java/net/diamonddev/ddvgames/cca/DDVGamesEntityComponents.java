package net.diamonddev.ddvgames.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.network.SyncLivesS2CPacket;
import net.diamonddev.ddvgames.network.SyncRoleS2CPacket;
import net.diamonddev.ddvgames.registry.InitPackets;
import net.diamonddev.libgenetics.common.api.v1.network.nerve.NerveNetworker;
import net.minecraft.server.network.ServerPlayerEntity;

public class DDVGamesEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<RoleComponent> ROLE =
            ComponentRegistryV3.INSTANCE.getOrCreate(DDVGamesMod.id("role"), RoleComponent.class);

    public static final ComponentKey<IntegerComponent> RISINGEDGE_LIVES =
            ComponentRegistryV3.INSTANCE.getOrCreate(DDVGamesMod.id("risingedge_lives"), IntegerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ROLE, player -> new RoleComponent(), RespawnCopyStrategy.ALWAYS_COPY);

        registry.registerForPlayers(RISINGEDGE_LIVES, player -> new IntegerComponent("Lives"), RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static Role getRole(ServerPlayerEntity player) {
        return ROLE.get(player).getRole();
    }

    public static String getRoleName(ServerPlayerEntity player) {
        return ROLE.get(player).getRoleString();
    }
    public static void setRole(ServerPlayerEntity player, Role role) {
        ROLE.get(player).setRole(role);

        SyncRoleS2CPacket.SyncRoleData data = new SyncRoleS2CPacket.SyncRoleData();
        data.roleName = getRoleName(player);
        data.playerUUID = player.getUuid();

        NerveNetworker.send(player, InitPackets.SYNC_ROLE, data);
    }


    public static int getLives(ServerPlayerEntity player) {
        return RISINGEDGE_LIVES.get(player).getInteger();
    }
    public static void setLives(ServerPlayerEntity player, int i) {
        RISINGEDGE_LIVES.get(player).setInteger(i);

        SyncLivesS2CPacket.SyncLivesData data = new SyncLivesS2CPacket.SyncLivesData();
        data.lives = getLives(player);
        data.playerUUID = player.getUuid();

        NerveNetworker.send(player, InitPackets.SYNC_LIVES, data);
    }


}
