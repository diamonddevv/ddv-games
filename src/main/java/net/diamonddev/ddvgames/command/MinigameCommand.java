package net.diamonddev.ddvgames.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.command.argument.MinigameArgType;
import net.diamonddev.ddvgames.command.argument.SettingArgType;
import net.diamonddev.ddvgames.minigame.Minigame;
import net.diamonddev.ddvgames.minigame.Setting;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MinigameCommand {

    private static final SimpleCommandExceptionType NOT_RUNNING = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.not_running"));
    private static final SimpleCommandExceptionType RUNNING = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.running"));
    private static final SimpleCommandExceptionType RUNNING_SET = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.running_set"));
    private static final SimpleCommandExceptionType NO_GAME = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.no_game"));


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("minigame").requires(source -> source.hasPermissionLevel(2)
                        ).then(literal("set")
                                .then(argument("minigame", MinigameArgType.minigame())
                                        .executes(MinigameCommand::exeSet)
                                )
                        ).then(literal("start")
                                .executes(MinigameCommand::exeStart)
                        ).then(literal("stop")
                                .executes(MinigameCommand::exeStop)
                        ).then(literal("settings")
                                .then(argument("setting", SettingArgType.setting())
                                        .then(argument("value", DoubleArgumentType.doubleArg())
                                                .executes(MinigameCommand::exeEditSettings)
                                        )
                                        .executes(MinigameCommand::exeGetSettingVal)
                                )
                        ).then(literal("get")
                                .executes(MinigameCommand::exeGet)
                        )
        );
    }


    public static int exeSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!DDVGamesMod.gameManager.isGameRunning()) {
            Minigame game = MinigameArgType.getMinigame(context, "minigame");
            DDVGamesMod.gameManager.setGame(game);
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.set_game", game.getName().getString(), game.getSemanticVersion().getString()), true);
            return 1;
        } else {
            throw RUNNING_SET.create();
        }
    }

    public static int exeGet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.hasGame()) {
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.get_game", DDVGamesMod.gameManager.getGame().getName().getString(),
                    DDVGamesMod.gameManager.getGame().getSemanticVersion()), false);
        } else {
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.get_game_none"), false);
        }
        return 1;
    }
    public static int exeStart(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        DDVGamesMod.gameManager.startGame();
        Minigame game = DDVGamesMod.gameManager.getGame();
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.start_game", game.getName().getString()), true);
        return 1;
    }
    public static int exeStop(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.isGameRunning()) {
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.stop_game", DDVGamesMod.gameManager.getGame().getName()), true);
            DDVGamesMod.gameManager.stopGame();
            return 1;
        } else {
            throw NOT_RUNNING.create();
        }

    }

    public static int exeEditSettings(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!DDVGamesMod.gameManager.isGameRunning()) {
            if (DDVGamesMod.gameManager.hasGame()) {
                double v = DoubleArgumentType.getDouble(context, "value");
                Setting setting = SettingArgType.getSetting(context, "setting");
                DDVGamesMod.gameManager.setSetting(setting.getSimpleName(), v);
                context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.setSettingVal", setting.getSimpleName(), v), true);
                return 1;
            } else {
                throw NO_GAME.create();
            }
        } else {
            throw RUNNING.create();
        }

    }

    public static int exeGetSettingVal(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.hasGame()) {
            Setting setting = SettingArgType.getSetting(context, "setting");
            double val = DDVGamesMod.gameManager.getSetting(setting.getSimpleName());
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.getSettingVal", setting.getSimpleName(), val), false);
            return 1;
        } else {
            throw NO_GAME.create();
        }
    }



}
