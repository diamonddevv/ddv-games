package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public abstract class StringArrayListArgType extends IdentifierArgumentType {

    public abstract ArrayList<String> getArray();

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(getArray(), builder);
    }
}
