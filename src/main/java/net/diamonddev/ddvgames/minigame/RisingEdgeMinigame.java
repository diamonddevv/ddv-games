package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.math.Cube;
import net.diamonddev.ddvgames.registry.InitRules;
import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.diamonddev.ddvgames.util.SharedUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.*;

import static net.diamonddev.ddvgames.minigame.Setting.*;

public class RisingEdgeMinigame extends Minigame {

    private static final String LIVES = "lives";
    private static final String GLOWING = "giveGlowing";
    private static final String HEALING = "naturalRegeneration";
    private static final String BORDER_DIST = "borderDistance";
    private static final String USE_HEIGHT_CONDITION = "warmupHeightCondition";
    private static final String WARMUP_CONDITION = "warmupCondition";
    private static final String RISE_INTERVAL = "riseInterval";

    public static final String SPECTATOR = "spectator";
    public static final String PLAYER = "player";

    public static final String WARMUP = "warmup";
    public static final String PVP = "pvp";

    private double previousBorderSize = 0.0;
    private Vec2f previousCenter = new Vec2f(0.0f, 0.0f);
    private GameRules previousRules;


    public Vec2f center;
    public double voidLevel = 0.0;
    private Vec3d spawnPoint;


    private double borderRadius;
    private final int[] elevationMilestones = new int[] {-32, -16, 0, 16, 64, 128};
    public RisingEdgeMinigame() {
        super(Text.translatable("ddv.minigame.rising_edge"), "0.0.1", SemanticVersioningSuffix.BETA);
    }

    @Override
    public ArrayList<Role> addRoles(ArrayList<Role> roles) {
        roles.add(new Role(SPECTATOR));
        roles.add(new Role(PLAYER));
        return roles;
    }

    @Override
    public ArrayList<Setting> addSettings(ArrayList<Setting> settings) {
        settings.add(new Setting(3.0, LIVES));
        settings.add(new Setting(0.0, GLOWING));
        settings.add(new Setting(1.0, HEALING));
        settings.add(new Setting(100.0, BORDER_DIST));
        settings.add(new Setting(1.0, USE_HEIGHT_CONDITION));
        settings.add(new Setting(64.0, WARMUP_CONDITION));
        settings.add(new Setting(3.0, RISE_INTERVAL));
        return settings;
    }

    @Override
    public ArrayList<GameState> addGameStates(ArrayList<GameState> states) {
        states.add(new GameState(WARMUP));
        states.add(new GameState(PVP));
        return states;
    }

    @Override
    public void onStart(Entity executor, Collection<PlayerEntity> players, World world) {
        double borderDist = parseAsDouble(BORDER_DIST);
        boolean glowing = parseAsBoolean(GLOWING);
        int lives = parseAsInt(LIVES);

        Collection<PlayerEntity> roledPlayers = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(PLAYER));
        Collection<PlayerEntity> roledSpectators = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(SPECTATOR));

        this.previousBorderSize = world.getWorldBorder().getSize();
        this.previousCenter = new Vec2f((float)world.getWorldBorder().getCenterX(), (float)world.getWorldBorder().getCenterZ());
        this.previousRules = world.getGameRules().copy();

        this.voidLevel = world.getBottomY() - 2;
        this.borderRadius = parseAsDouble(BORDER_DIST) / 2;

        this.center = new Vec2f((float) executor.getX(), (float) executor.getZ());
        this.timer = 0.0;

        // Set players spawns
        this.spawnPoint = SharedUtil.addY(this.center, executor.getY());
        players.forEach(player -> {
            ServerPlayerEntity spe = SharedUtil.getServerPlayer(player, (ServerWorld) world);
            spe.setSpawnPoint(spe.getWorld().getRegistryKey(), SharedUtil.vecToBlockPos(this.spawnPoint),
                    0.0f, true, false);
        });

        world.getWorldBorder().setCenter(Math.round(executor.getX()), Math.round(executor.getZ()));
        world.getWorldBorder().setSize(borderDist);

        DDVGamesMod.gameManager.switchState(GameState.fromName(WARMUP), world);

        players.forEach(player -> player.teleport(executor.getX(), executor.getY(), executor.getZ()));

        // Spectators in Spectator Mode
        roledSpectators.forEach(player -> SharedUtil.changePlayerGamemode(player, GameMode.SPECTATOR));

        // Players in Survival Mode
        roledPlayers.forEach(player -> SharedUtil.changePlayerGamemode(player, GameMode.SURVIVAL));

        // Glowing
        roledPlayers.forEach(player -> player.setGlowing(true));

        // Set Lives
        roledPlayers.forEach(player -> DDVGamesEntityComponents.setLives(player, lives));

        // Clear all players inventories
        roledPlayers.forEach(player -> player.getInventory().clear());
    }

    @Override
    public void onEnd(Collection<PlayerEntity> players, World world) {

        world.getWorldBorder().setCenter(this.previousCenter.x, this.previousCenter.y);
        world.getWorldBorder().setSize(this.previousBorderSize);
        players.forEach(player -> DDVGamesEntityComponents.setLives(player, 0));

        // Unglowing
        players.forEach(player -> player.setGlowing(false));

        writeGamerules(this.previousRules, world);
    }

    @Override
    public boolean canWin(PlayerEntity winnerCandidate, Collection<PlayerEntity> players) {
        Collection<PlayerEntity> cpe = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(PLAYER));
        cpe.remove(winnerCandidate);
        return cpe.isEmpty();
    }

    @Override
    public void onWin(PlayerEntity winningPlayer, World world, Collection<PlayerEntity> players) {
        players.forEach(player -> player.sendMessage(Text.translatable("ddv.minigame.rising_edge.win_title", winningPlayer.getGameProfile().getName()), true));

        ServerWorld serverWorld = null;
        try { serverWorld = Objects.requireNonNull(world.getServer()).getWorld(winningPlayer.getWorld().getRegistryKey());
        } catch (Exception ignored) {}

        if (serverWorld != null) {
            SharedUtil.spawnParticle(serverWorld, ParticleTypes.TOTEM_OF_UNDYING, winningPlayer.getPos(), SharedUtil.cubeVec(0.5), 5000, 0.5);
            serverWorld.playSound(
                    null,
                    winningPlayer.getX(), winningPlayer.getY(), winningPlayer.getZ(),
                    SoundEvents.MUSIC_DISC_PIGSTEP, SoundCategory.MUSIC,
                    20.0f, 2.0f);
        }

        players.forEach(player -> player.getInventory().clear());
    }

    @Override
    public void tickClock(World world) { // TICK CLOCK
        boolean heightCondition = parseAsBoolean(USE_HEIGHT_CONDITION);
        int warmupCondition = heightCondition ? parseAsInt(WARMUP_CONDITION) : parseAsInt(WARMUP_CONDITION) * 20;
        int ascensionInterval = parseAsInt(RISE_INTERVAL) * 20;

        if (this.getTicks() % 20 == 0) { // For-each Second Recursion Loop
            if (!DDVGamesMod.gameManager.getCurrentState().getName().matches(PVP)) {
                if (!heightCondition) {
                    if (this.getTicks() >= warmupCondition) {
                        DDVGamesMod.gameManager.switchState(GameState.fromName(PVP), world);
                    }
                } else {
                    if (voidLevel >= warmupCondition) {
                        DDVGamesMod.gameManager.switchState(GameState.fromName(PVP), world);
                    }
                }
            }
        }

        if (this.getTicks() % 2 == 0) { // For-each Tenth-Second Recursion Loop
            this.timer += 0.1;
        }

        if (this.getTicks() % ascensionInterval == 0) {
            voidLevel += 1.0;
            voidRise(world, DDVGamesMod.gameManager.getPlayers());
        }

        killFallingGravityBlocksBeneathVoid(world);
    }

    @Override
    public boolean canStart(Collection<PlayerEntity> players) {
        return DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(PLAYER)).size() > 1;
    }

    @Override
    public String getDefaultRoleName() {
        return PLAYER;
    }

    @Override
    public String getStartingStateName() {
        return WARMUP;
    }

    @Override
    public void onStateStarts(GameState state, World world) {
        if (state.getName().matches(WARMUP)) {
            GameRules warmup = new GameRules();
            warmup.get(InitRules.PVP).set(false, world.getServer());
            warmup.get(GameRules.NATURAL_REGENERATION).set(true, world.getServer());
            warmup.get(GameRules.KEEP_INVENTORY).set(true, world.getServer());
            warmup.get(GameRules.DO_IMMEDIATE_RESPAWN).set(true, world.getServer());

            writeGamerules(warmup, world);

            DDVGamesMod.gameManager.getPlayers().forEach(player -> player.sendMessage(Text.translatable("ddv.minigame.rising_edge.warmup_start")));

        } else if (state.getName().matches(PVP)) {
            GameRules pvp = new GameRules();
            pvp.get(InitRules.PVP).set(true, world.getServer());
            pvp.get(GameRules.NATURAL_REGENERATION).set(parseAsBoolean(HEALING), world.getServer());

            writeGamerules(pvp, world);

            this.enablePvp(DDVGamesMod.gameManager.getPlayers());
        }
    }

    @Override
    public void onStateEnds(GameState state, World world) {
    }

    private void voidRise(World world, Collection<PlayerEntity> players) {

        // Block Deletion
        Cube cube = SharedUtil.createCube(this.borderRadius, this.center, this.voidLevel, world.getBottomY());


        BlockPos.iterate(new BlockPos(cube.getCornerA()), new BlockPos(cube.getCornerB())).forEach(pos -> {
            if (!world.getBlockState(pos).isAir()) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        });

        // Play Sound
        players.forEach(player -> player.playSound(
                SoundEvents.ENTITY_EVOKER_FANGS_ATTACK,
                SoundCategory.NEUTRAL,
                20.0f,
                1.0f)
        );


        // Inform Players
        if (Arrays.stream(this.elevationMilestones).anyMatch(value -> value == this.voidLevel)) {
            // might keep as a permanent thing saying height as overlay and scrap chat message
            players.forEach(player -> player.sendMessage(Text.translatable("ddv.minigame.rising_edge.rise_milestone", this.voidLevel), true));
        } else {
            players.forEach(player -> player.sendMessage(Text.translatable("ddv.minigame.rising_edge.rise"), true));
        }


        // I removed the particle effect from the datapack version, it probably would cause too much lag on top of the mass block replacing
    }

    private void killFallingGravityBlocksBeneathVoid(World world) {
        Cube rise = SharedUtil.createCube(borderRadius, this.center, this.voidLevel, world.getBottomY());
        List<FallingBlockEntity> entities = world.getEntitiesByClass(FallingBlockEntity.class,
                rise, entity -> true);
        entities.forEach(Entity::kill);
    }

    private void enablePvp(Collection<PlayerEntity> players) {
        players.forEach(player -> {
            SharedUtil.pushPlayerTitle(player, Text.translatable("ddv.minigame.rising_edge.pvp_enabled"));
            SharedUtil.pushPlayerSubtitle(player, Text.translatable("ddv.minigame.rising_edge.pvp_enabled.sub"));
            player.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 10.0f, 0.5f);
        });
    }

    private void writeGamerules(GameRules n, World world) {
        world.getGameRules().setAllValues(n, world.getServer());
    }
}
