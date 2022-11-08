package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.concurrent.CompletableFuture;


public class MinigameArgType extends IdentifierArgumentType { // Since IdentifierArgumentType exists, its mine now

    private static final DynamicCommandExceptionType INVALID_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.argument.invalid_game", id));

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
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(InitRegistries.MINIGAMES.getIds(), builder);
    }
}
