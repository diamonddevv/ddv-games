package net.diamonddev.ddvgames.registry;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.command.MinigameCommand;
import net.diamonddev.ddvgames.command.argument.*;
import net.diamonddev.ddvgames.minigame.GameState;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class InitCommand implements RegistryInitializer {
    @Override
    public void register() {
        // ARGS
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id("minigame_command_arg"),
                MinigameArgType.class, ConstantArgumentSerializer.of(MinigameArgType::minigame));
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id("setting_command_arg"),
                SettingArgType.class, ConstantArgumentSerializer.of(SettingArgType::setting));
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id("role_command_arg"),
                RoleArgType.class, ConstantArgumentSerializer.of(RoleArgType::role));
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id("gamestate_command_arg"),
                GameStateArgType.class, ConstantArgumentSerializer.of(GameStateArgType::gamestate));
        ArgumentTypeRegistry.registerArgumentType(DDVGamesMod.id("settingset_command_arg"),
                SettingsSetArgType.class, ConstantArgumentSerializer.of(SettingsSetArgType::settingset));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // Register Commands Here
            MinigameCommand.register(dispatcher);


        });
    }
}
