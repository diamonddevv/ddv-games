package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.command.MinigameCommand;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class InitCommand implements RegistryInitializer {
    @Override
    public void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // Register Commands Here
            MinigameCommand.register(dispatcher);


        });
    }
}
