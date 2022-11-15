package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.diamonddev.ddvgames.command.argument.abstraction.RegistryArgType;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class MinigameArgType extends RegistryArgType { // Since IdentifierArgumentType exists, its mine now

    private static final DynamicCommandExceptionType INVALID_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.argument.invalid_game", id));

    private MinigameArgType() {}

    public static MinigameArgType minigame() {
        return new MinigameArgType();
    }
    public static Minigame getMinigame(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Identifier identifier = getIdentifier(context, argumentName);
        Minigame minigame = InitRegistries.MINIGAMES.get(identifier);
        if (minigame == null) {
            throw INVALID_EXCEPTION.create(identifier);
        } else {
            return minigame;
        }
    }


    @Override
    public Registry<?> getRegistry() {
        return InitRegistries.MINIGAMES;
    }

}
