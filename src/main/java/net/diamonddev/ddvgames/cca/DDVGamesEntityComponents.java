package net.diamonddev.ddvgames.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Role;
import net.diamonddev.ddvgames.network.NetcodeConstants;
import net.diamonddev.ddvgames.network.SyncLivesS2CPacket;
import net.diamonddev.ddvgames.network.SyncRoleS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class DDVGamesEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<RoleComponent> ROLE =
            ComponentRegistryV3.INSTANCE.getOrCreate(DDVGamesMod.id.build("role"), RoleComponent.class);

    public static final ComponentKey<IntegerComponent> RISINGEDGE_LIVES =
            ComponentRegistryV3.INSTANCE.getOrCreate(DDVGamesMod.id.build("risingedge_lives"), IntegerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ROLE, player -> new RoleComponent(), RespawnCopyStrategy.ALWAYS_COPY);

        registry.registerForPlayers(RISINGEDGE_LIVES, player -> new IntegerComponent("Lives"), RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static Role getRole(PlayerEntity player) {
        return ROLE.get(player).getRole();
    }
    public static void setRole(PlayerEntity player, Role role) {
        ROLE.get(player).setRole(role);
        ServerPlayNetworking.send((ServerPlayerEntity) player, NetcodeConstants.SYNC_ROLES_ID, SyncRoleS2CPacket.write(getRole(player), player));
    }


    public static int getLives(PlayerEntity player) {
        return RISINGEDGE_LIVES.get(player).getInteger();
    }
    public static void setLives(PlayerEntity player, int i) {
        RISINGEDGE_LIVES.get(player).setInteger(i);
        ServerPlayNetworking.send((ServerPlayerEntity) player, NetcodeConstants.SYNC_LIVES_ID, SyncLivesS2CPacket.write(getLives(player), player));
    }


}
