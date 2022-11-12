package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.command.MinigameCommand;
import net.diamonddev.ddvgames.command.argument.MinigameArgType;
import net.diamonddev.ddvgames.command.argument.RoleArgType;
import net.diamonddev.ddvgames.command.argument.SettingArgType;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class InitCommand implements RegistryInitializer {
    @Override
    public void register() {
        // ARGS
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id.build("minigame_command_arg"),
                MinigameArgType.class, ConstantArgumentSerializer.of(MinigameArgType::minigame));
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id.build("setting_command_arg"),
                SettingArgType.class, ConstantArgumentSerializer.of(SettingArgType::setting));
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id.build("role_command_arg"),
                RoleArgType.class, ConstantArgumentSerializer.of(RoleArgType::role));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // Register Commands Here
            MinigameCommand.register(dispatcher);


        });
    }
}
