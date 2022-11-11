package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public abstract class StringArrayListArgType implements ArgumentType<String> {

    private static SimpleCommandExceptionType COMMAND_EXCEPTION;

    public abstract ArrayList<String> getArray();

    private static final char[] allowedChars = { // allow 'camelCase' characters. Generated with a Python Script. May be Expanded.
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', // Integers
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', // Uppercase
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' // Lowercase
    };
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(getArray(), builder);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while (reader.canRead() && isValidChar(reader.peek())) {
            reader.skip();
        }

        return reader.getString().substring(i, reader.getCursor());
    }

    private static boolean isValidChar(char c) {
        for (char test : allowedChars) {
            if (test == c) {
                return true;
            }
        }
        return false;
    }

}
