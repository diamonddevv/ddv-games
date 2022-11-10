package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.util.registry.Registry;

import java.util.concurrent.CompletableFuture;

public abstract class RegistryArgType extends IdentifierArgumentType {

    public abstract Registry<?> getRegistry();

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(getRegistry().getIds(), builder);
    }

}
