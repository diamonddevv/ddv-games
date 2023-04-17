package net.diamonddev.ddvgames.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.GameState;
import net.diamonddev.libgenetics.core.command.abstraction.StringArrayListArgType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class GameStateArgType extends StringArrayListArgType {
    private static final DynamicCommandExceptionType INVALID_EXCEPTION =
            new DynamicCommandExceptionType((id) -> Text.translatable("ddv.argument.invalid_setting", id));

    private GameStateArgType() {}

    public static GameStateArgType gamestate() {
        return new GameStateArgType();
    }


    public static GameState getState(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Collection<GameState> settings = DDVGamesMod.gameManager.getStates();
        String simpleName = context.getArgument(argumentName, String.class);
        GameState state = null;
        for (GameState s : settings) {
            if (s.name().matches(simpleName)) {
                state = s;
            }
        }

        if (state == null) {
            throw INVALID_EXCEPTION.create(simpleName);
        } else {
            return state;
        }
    }


    @Override
    public ArrayList<String> getArray() {
        ArrayList<String> names = new ArrayList<>();
        if (DDVGamesMod.gameManager.hasGame()) {
            DDVGamesMod.gameManager.getStates().forEach(state -> names.add(state.name()));
        }
        return names;
    }
}
