package net.diamonddev.ddvgames.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Role;
import net.minecraft.entity.player.PlayerEntity;

public class DDVGamesEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<RoleComponent> ROLE =
            ComponentRegistryV3.INSTANCE.getOrCreate(DDVGamesMod.id.build("role"), RoleComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, ROLE, player -> new RoleComponent());
    }

    public static Role getRole(PlayerEntity player) {
        return ROLE.get(player).getRole();
    }

    public static void setRole(PlayerEntity player, Role role) {
        ROLE.get(player).setRole(role);
    }
}
