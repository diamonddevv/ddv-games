package net.diamonddev.ddvgames.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.command.argument.*;
import net.diamonddev.ddvgames.minigame.*;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MinigameCommand {

    private static final SimpleCommandExceptionType CANNOT_PAUSE = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.cannot_pause"));
    private static final SimpleCommandExceptionType NOT_RUNNING = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.not_running"));
    private static final SimpleCommandExceptionType RUNNING = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.running"));
    private static final SimpleCommandExceptionType RUNNING_SET = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.running_set"));
    private static final SimpleCommandExceptionType NO_GAME = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.no_game"));
    private static final SimpleCommandExceptionType CANNOT_START = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.cannot_start"));
    private static final SimpleCommandExceptionType ALREADY_RUNNING = new SimpleCommandExceptionType(Text.translatable("ddv.command.exception.already_running"));


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("minigame").requires(source -> source.hasPermissionLevel(2)
                        ).then(literal("load")
                                .then(argument("minigame", MinigameArgType.minigame())
                                        .executes(MinigameCommand::exeSet)
                                )
                                .executes(MinigameCommand::exeUnload)
                        ).then(literal("start")
                                .then(literal("quick")
                                        .then(argument("minigame", MinigameArgType.minigame())
                                                .then(argument("settingset", SettingsSetArgType.settingset())
                                                        .executes(MinigameCommand::exeQuickStartWithSettingSet)
                                                )
                                                .executes(MinigameCommand::exeQuickStart)
                                        )
                                ).executes(MinigameCommand::exeStart)
                        ).then(literal("stop")
                                .executes(MinigameCommand::exeStop)
                        ).then(literal("settings")
                                .then(literal("settings")
                                        .then(argument("setting", SettingArgType.setting())
                                                .then(argument("value", DoubleArgumentType.doubleArg())
                                                        .executes(MinigameCommand::exeEditSettings)
                                                )
                                                .executes(MinigameCommand::exeGetSettingVal)
                                        )
                                ).then(literal("settingset")
                                        .then(argument("settingset", SettingsSetArgType.settingset())
                                                .executes(MinigameCommand::exeSetSettingsSet)
                                        )
                                )
                        ).then(literal("get")
                                .executes(MinigameCommand::exeGet)
                        ).then(literal("players")
                                .then(literal("add")
                                        .then(argument("players", EntityArgumentType.players())
                                                .then(argument("role", RoleArgType.role())
                                                        .executes(MinigameCommand::exeAddPlayersWithRole)
                                                )
                                                .executes(MinigameCommand::exeAddPlayers)
                                        )
                                ).then(literal("remove")
                                        .then(argument("players", EntityArgumentType.players())
                                                .executes(MinigameCommand::exeRemovePlayers)
                                        )
                                ).then(literal("clear")
                                        .executes(MinigameCommand::exeRemoveAll)
                                ).then(literal("get")
                                        .executes(MinigameCommand::exeGetPlayers)
                                )
                        ).then(literal("roles")
                                .then(literal("attach")
                                        .then(argument("players", EntityArgumentType.players())
                                                .then(argument("role", RoleArgType.role())
                                                        .executes(MinigameCommand::exeAddPlayersRole)
                                                )
                                        )
                                ).then(literal("detach")
                                        .then(argument("players", EntityArgumentType.players())
                                                .executes(MinigameCommand::exeRemovePlayersRole)
                                        )
                                ).then(literal("getplayers")
                                        .then(argument("role", RoleArgType.role())
                                                .executes(MinigameCommand::exeGetPlayersInRole)
                                        )
                                ).then(literal("getrole")
                                        .then(argument("player", EntityArgumentType.player())
                                                .executes(MinigameCommand::exeGetPlayerRole)
                                        )
                                )
                        ).then(literal("states")
                                .then(literal("jump")
                                        .then(argument("state", GameStateArgType.gamestate())
                                                .executes(MinigameCommand::exeJumpToState)
                                        )
                                ).then(literal("get")
                                        .executes(MinigameCommand::exeGetState)
                                )
                        ).then(literal("togglepause")
                                .executes(MinigameCommand::exeTogglePause)
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

    public static int exeUnload(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (!DDVGamesMod.gameManager.isGameRunning()) {
            DDVGamesMod.gameManager.setGame(null);
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.unloaded_game"), true);
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
        if (DDVGamesMod.gameManager.isGameRunning()) {
            throw ALREADY_RUNNING.create();
        }

        if (DDVGamesMod.gameManager.getGame().canStart(DDVGamesMod.gameManager.getPlayers())) {
            DDVGamesMod.gameManager.startGame(context.getSource().getEntity(), context.getSource().getWorld());
            Minigame game = DDVGamesMod.gameManager.getGame();
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.start_game", game.getName().getString()), true);
            return 1;
        } else {
            throw CANNOT_START.create();
        }
    }
    public static int exeQuickStart(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.isGameRunning()) {
            throw ALREADY_RUNNING.create();
        }

        Minigame game = MinigameArgType.getMinigame(context, "minigame");

        DDVGamesMod.gameManager.setGame(game);
        DDVGamesMod.gameManager.removeRolesAndPlayers(DDVGamesMod.gameManager.getServerPlayers());
        DDVGamesMod.gameManager.addPlayersWithRole(context.getSource().getServer().getPlayerManager().getPlayerList(), DDVGamesMod.gameManager.getDefaultRole());


        if (!game.canStart(DDVGamesMod.gameManager.getPlayers())) throw CANNOT_START.create();

        DDVGamesMod.gameManager.startGame(context.getSource().getEntity(), context.getSource().getWorld());

        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.quickstart_game", game.getName().getString(), game.getSemanticVersion().getString()), true);
        return 1;
    }

    public static int exeQuickStartWithSettingSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.isGameRunning()) {
            throw ALREADY_RUNNING.create();
        }

        Minigame game = MinigameArgType.getMinigame(context, "minigame");
        SettingsSet settingsSet = SettingsSetArgType.getSettingsSet(context, "settingset", InitRegistries.MINIGAMES.getId(game));

        DDVGamesMod.gameManager.setGame(game);
        DDVGamesMod.gameManager.removeRolesAndPlayers(DDVGamesMod.gameManager.getServerPlayers());
        DDVGamesMod.gameManager.addPlayersWithRole(context.getSource().getServer().getPlayerManager().getPlayerList(), DDVGamesMod.gameManager.getDefaultRole());

        if (!game.canStart(DDVGamesMod.gameManager.getPlayers())) throw CANNOT_START.create();

        String name = null, author = null;
        Text text;

        if (settingsSet.hasNameData()) name = settingsSet.getSetName();
        if (settingsSet.hasAuthorData()) author = settingsSet.getSetAuthor();

        if (name == null && author == null) {
            text = Text.translatable("ddv.command.feedback.quickstart_game", game.getName().getString(), game.getSemanticVersion().getString());
        } else if (name == null) {
            text = Text.translatable("ddv.command.feedback.quickstart_game.settingset.author", game.getName().getString(), game.getSemanticVersion().getString(), author);
        } else if (author == null) {
            text = Text.translatable("ddv.command.feedback.quickstart_game.settingset.name", game.getName().getString(), game.getSemanticVersion().getString(), name);
        } else {
            text = Text.translatable("ddv.command.feedback.quickstart_game.settingset.name_author", game.getName().getString(), game.getSemanticVersion().getString(), name, author);
        }

        DDVGamesMod.gameManager.setAllSettings(settingsSet);

        DDVGamesMod.gameManager.startGame(context.getSource().getEntity(), context.getSource().getWorld());

        context.getSource().sendFeedback(text, true);
        return 1;
    }
    public static int exeStop(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.getGameHasStarted()) {
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.stop_game", DDVGamesMod.gameManager.getGame().getName()), true);
            DDVGamesMod.gameManager.stopGame(context.getSource().getWorld());
            return 1;
        } else {
            throw NOT_RUNNING.create();
        }

    }


    public static int exeTogglePause(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.getGameHasStarted()) {
            Minigame game = DDVGamesMod.gameManager.getGame();
            boolean isPausing = game.isRunning();
            game.togglePause();
            Text op = isPausing ? Text.translatable("ddv.command.feedback.pause") : Text.translatable("ddv.command.feedback.play");
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.paused", op.getString()), true);
            return 1;
        } else {
            throw CANNOT_PAUSE.create();
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

    public static int exeSetSettingsSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (DDVGamesMod.gameManager.hasGame()) {
            SettingsSet settingsSet = SettingsSetArgType.getSettingsSet(context, "settingset", DDVGamesMod.gameManager.getCurrentGameId());
            DDVGamesMod.gameManager.setAllSettings(settingsSet);

            String name = null, author = null;
            Text text;

            if (settingsSet.hasNameData()) name = settingsSet.getSetName();
            if (settingsSet.hasAuthorData()) author = settingsSet.getSetAuthor();

            if (name == null && author == null) {
                text = Text.translatable("ddv.command.feedback.applySettingset");
            } else if (name == null) {
                text = Text.translatable("ddv.command.feedback.applySettingset.author", author);
            } else if (author == null) {
                text = Text.translatable("ddv.command.feedback.applySettingset.name", name);
            } else {
                text = Text.translatable("ddv.command.feedback.applySettingset.name_author", name, author);
            }



            context.getSource().sendFeedback(text, true);
            return 1;
        } else {
            throw NO_GAME.create();
        }
    }


    public static int exeAddPlayersWithRole(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        Role role = RoleArgType.getRole(context, "role");

        players.removeIf(player -> DDVGamesMod.gameManager.getPlayers().contains(player));
        DDVGamesMod.gameManager.addPlayersWithRole(players, role);
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.added_players"), true);
        return 1;
    }
    public static int exeAddPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        players.removeIf(player -> DDVGamesMod.gameManager.getPlayers().contains(player));
        DDVGamesMod.gameManager.addPlayers(players);
        players.forEach(serverPlayerEntity -> DDVGamesMod.gameManager.attachRole(serverPlayerEntity, DDVGamesMod.gameManager.getDefaultRole()));
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.added_players"), true);
        return 1;
    }
    public static int exeRemovePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        players.removeIf(player -> !DDVGamesMod.gameManager.getPlayers().contains(player));
        players.forEach(player -> DDVGamesMod.gameManager.detachRole(player));
        DDVGamesMod.gameManager.removePlayers(players);
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.removed_players"), true);
        return 1;
    }
    public static int exeRemoveAll(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        DDVGamesMod.gameManager.getPlayers().forEach(player -> DDVGamesMod.gameManager.detachRole(player));
        DDVGamesMod.gameManager.getPlayers().clear();
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.cleared_players"), true);
        return 1;
    }
    public static int exeGetPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int playerCount = 0;
        StringBuilder playerNames = new StringBuilder();
        String splitter = ", ";


        for (PlayerEntity player : DDVGamesMod.gameManager.getPlayers()) {
            playerCount++;
            String thisName = player.getGameProfile().getName();
            playerNames.append(thisName);
            if (DDVGamesMod.gameManager.getPlayers().size() > playerCount) {
                playerNames.append(splitter);
            }
        }

        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.get_players", playerCount, playerNames.toString()), true);
        return 1;
    }


    public static int exeGetPlayersInRole(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Role role = RoleArgType.getRole(context, "role");

        int playerCount = 0;
        StringBuilder playerNames = new StringBuilder();
        String splitter = ", ";
        Collection<ServerPlayerEntity> players = DDVGamesMod.gameManager.getServerPlayers();

        players.removeIf(player -> !DDVGamesEntityComponents.getRoleName(player).matches(role.getName()));


        for (PlayerEntity player : players) {
            playerCount++;
            String thisName = player.getGameProfile().getName();
            playerNames.append(thisName);
            if (players.size() > playerCount) {
                playerNames.append(splitter);
            }
        }

        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.get_players_role", playerCount, role.getName(), playerNames.toString()), true);
        return 1;
    }

    public static int exeGetPlayerRole(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        Role role = DDVGamesEntityComponents.getRole(player);

        if (Objects.equals(role.getName(), "")) {
            context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.player_has_no_role",
                    player.getGameProfile().getName()), false);
            return 1;
        }

        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.get_player_role",
                player.getGameProfile().getName(), role.getName()), false);
        return 1;
    }
    public static int exeAddPlayersRole(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        Role role = RoleArgType.getRole(context, "role");

        players.forEach(player -> DDVGamesMod.gameManager.attachRole(player, role));
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.add_players_role", role.getName()), true);
        return 1;
    }
    public static int exeRemovePlayersRole(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");

        players.forEach(player -> DDVGamesMod.gameManager.detachRole(player));
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.remove_players_role"), true);
        return 1;
    }


    public static int exeJumpToState(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        GameState state = GameStateArgType.getState(context, "state");

        DDVGamesMod.gameManager.switchState(state, context.getSource().getWorld());
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.jumpState", state.getName()), true);
        return 1;
    }

    public static int exeGetState(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        GameState state = DDVGamesMod.gameManager.getCurrentState();
        context.getSource().sendFeedback(Text.translatable("ddv.command.feedback.getState", state.getName()), true);
        return 1;
    }

}
