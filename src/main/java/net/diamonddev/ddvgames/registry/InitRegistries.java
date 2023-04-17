package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

public class InitRegistries implements RegistryInitializer {
    public static Registry<Minigame> MINIGAMES;


    @Override
    public void register() {
        MINIGAMES = FabricRegistryBuilder.createSimple(
                Minigame.class,
                DDVGamesMod.id("minigames")
        ).buildAndRegister();

    }
}
