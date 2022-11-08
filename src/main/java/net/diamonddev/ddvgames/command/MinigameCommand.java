package net.diamonddev.ddvgames.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.command.argument.MinigameArgType;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MinigameCommand {

    private static final SimpleCommandExceptionType NOT_RUNNING = new SimpleCommandExceptionType(Text.translatable("ddv.argument.not_running"));


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("minigame").requires(source -> source.hasPermissionLevel(2)
                        ).then(literal("set")
                                .then(argument("minigame", MinigameArgType.identifier())
                                        .executes(MinigameCommand::exeSet)
                                )
                        ).then(literal("start")
                                .executes(MinigameCommand::exeStart)
                        ).then(literal("stop")
                                .executes(MinigameCommand::exeStop)
                        )
        );
    }


    public static int exeSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Minigame game = MinigameArgType.getMinigame(context, "minigame");
        DDVGamesMod.gameManager.setGame(game);
        context.getSource().sendFeedback(Text.translatable("ddv.command.set_game", game.getName().getString()), true);
        return 1;
    }

    public static int exeStart(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        DDVGamesMod.gameManager.startGame();
        Minigame game = DDVGamesMod.gameManager.getGame();
        context.getSource().sendFeedback(Text.translatable("ddv.command.start_game", game.getName().getString()), true);
        return 1;
    }

    public static int exeStop(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.isGameRunning()) {
            DDVGamesMod.gameManager.stopGame();
            return 1;
        } else {
            throw NOT_RUNNING.create();
        }

    }
}
