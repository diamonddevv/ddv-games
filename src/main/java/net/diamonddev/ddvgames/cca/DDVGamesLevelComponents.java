package net.diamonddev.ddvgames.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.diamonddev.ddvgames.DDVGamesMod;

public class DDVGamesLevelComponents implements LevelComponentInitializer {

    public static final ComponentKey<GameManagerComponent> GAME_MANAGER =
            ComponentRegistryV3.INSTANCE.getOrCreate(DDVGamesMod.id.build("gamemanager"), GameManagerComponent.class);


    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(GAME_MANAGER, worldProperties -> new GameManagerComponent());
    }
}
