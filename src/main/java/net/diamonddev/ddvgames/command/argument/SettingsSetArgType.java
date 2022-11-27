package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.diamonddev.ddvgames.command.argument.abstraction.StringArrayListArgType;
import net.diamonddev.ddvgames.minigame.SettingsSet;
import net.diamonddev.ddvgames.registry.InitResourceManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingsSetArgType extends StringArrayListArgType {

    private static final DynamicCommandExceptionType INVALID_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.command.exception.invalid_settingset", id));
    private static final DynamicCommandExceptionType INVALIDGAME_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.command.exception.invalid_game_settingset", id));

    public static SettingsSetArgType settingset() {
        return new SettingsSetArgType();
    }

    private static Identifier id;

    public static SettingsSet getSettingsSet(CommandContext<ServerCommandSource> context, String argumentName, Identifier gameId) throws CommandSyntaxException {
        String simpleName = context.getArgument(argumentName, String.class);
        SettingsSet set = null;
        for (Map.Entry<String, SettingsSet> s : InitResourceManager.RESOURCE_SETTINGSSET.entrySet()) {
            if (s.getKey().matches(simpleName)) {
                set = s.getValue();
            }
        }

        if (set == null) {
            throw INVALID_EXCEPTION.create(simpleName);
        } else {
            if (!set.getId().toString().matches(gameId.toString())) {
                throw INVALIDGAME_EXCEPTION.create(set.getId());
            }
            id = set.getId();
            return set;
        }

    }

    @Override
    public ArrayList<String> getArray() {
        return new ArrayList<String>(InitResourceManager.RESOURCE_SETTINGSSET.keySet());
    }
}
