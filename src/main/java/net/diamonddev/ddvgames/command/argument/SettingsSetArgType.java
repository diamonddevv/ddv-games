package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.diamonddev.ddvgames.command.argument.abstraction.StringArrayListArgType;
import net.diamonddev.ddvgames.minigame.SettingsSet;
import net.diamonddev.ddvgames.registry.InitResourceManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class SettingsSetArgType extends StringArrayListArgType {

    private static final DynamicCommandExceptionType INVALID_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.command.exception.invalid_settingset", id));

    public static SettingsSetArgType settingset() {
        return new SettingsSetArgType();
    }


    public static SettingsSet getSettingsSet(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        String simpleName = context.getArgument(argumentName, String.class);
        SettingsSet set = null;
        for (SettingsSet s : InitResourceManager.RESOURCE_SETTINGSSET) {
            if (s.getId().toString().matches(argumentName)) {
                set = s;
            }
        }

        if (set == null) {
            throw INVALID_EXCEPTION.create(simpleName);
        } else {
            return set;
        }

    }

    @Override
    public ArrayList<String> getArray() {
        return InitResourceManager.RESOURCE_SETTINGSSET_KEYS;
    }
}
