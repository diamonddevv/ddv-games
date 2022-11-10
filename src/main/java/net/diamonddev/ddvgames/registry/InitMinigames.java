package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.minigame.RisingEdgeMinigame;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.minecraft.util.registry.Registry;

public class InitMinigames implements RegistryInitializer {

    public static Minigame RISING_EDGE = new RisingEdgeMinigame();
    @Override
    public void register() {
        Registry.register(InitRegistries.MINIGAMES, DDVGamesMod.id.build("rising_edge"), RISING_EDGE);
    }
}
