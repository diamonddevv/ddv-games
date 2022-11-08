package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.minigame.TestMinigame;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.minecraft.util.registry.Registry;

public class InitMinigames implements RegistryInitializer {

    public static Minigame TEST = new TestMinigame();
    @Override
    public void register() {
        Registry.register(InitRegistries.MINIGAMES, DDVGamesMod.id.build("test"), TEST);
    }
}
