package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.Setting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class SettingArgType extends StringArrayListArgType {

    private static final DynamicCommandExceptionType INVALID_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.argument.invalid_setting", id));

    private SettingArgType() {}

    public static SettingArgType setting() {
        return new SettingArgType();
    }


    public static Setting getSetting(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Collection<Setting> settings = DDVGamesMod.gameManager.getSettings();
        String simpleName = context.getArgument(argumentName, String.class);
        Setting setting = null;
        System.out.println(simpleName);
        for (Setting s : settings) {
            if (s.getSimpleName().matches(simpleName)) {
                setting = s;
            }
        }

        if (setting == null) {
            throw INVALID_EXCEPTION.create(simpleName);
        } else {
            return setting;
        }
    }


    @Override
    public ArrayList<String> getArray() {
        ArrayList<String> names = new ArrayList<>();
        if (DDVGamesMod.gameManager.hasGame()) {
            DDVGamesMod.gameManager.getSettings().forEach(setting -> names.add(setting.getSimpleName()));
        }
        return names;
    }
}
