package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.AbstractMinigame;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;

public class InitRegistries implements RegistryInitializer {
    public static Registry<AbstractMinigame> MINIGAMES;


    @Override
    public void register() {
        MINIGAMES = FabricRegistryBuilder.createSimple(
                AbstractMinigame.class,
                DDVGamesMod.id.build("minigames")
        ).buildAndRegister();
    }
}
