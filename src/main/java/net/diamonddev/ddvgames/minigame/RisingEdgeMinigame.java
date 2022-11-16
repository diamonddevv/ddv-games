package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.cca.DDVGamesEntityComponents;
import net.diamonddev.ddvgames.registry.InitRules;
import net.diamonddev.ddvgames.math.Quadrilateral;
import net.diamonddev.ddvgames.util.SharedUtil;
import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.*;

import static net.diamonddev.ddvgames.minigame.Setting.*;

public class RisingEdgeMinigame extends Minigame {

    private static final String LIVES = "livesPerPlayer";
    private static final String GLOWING = "giveGlowing";
    private static final String HEALING = "allowHealing";
    private static final String BORDER_DIST = "borderDistance";
    private static final String WARMUP_SECONDS = "warmupSeconds";
    private static final String RISE_INTERVAL = "riseInterval";

    public static final String SPECTATOR = "spectator";
    public static final String PLAYER = "player";

    public static final String WARMUP = "warmup";
    public static final String PVP = "pvp";

    private double previousBorderSize = 0.0;
    private Vec2f previousCenter = new Vec2f(0.0f, 0.0f);
    private GameRules previousRules;
    private static final StatusEffectInstance GLOWING_EFFECT_INSTANCE =
            new StatusEffectInstance(StatusEffects.GLOWING, 100000, 1, true, false, false);


    public Vec2f center;
    public double voidLevel = 0.0;
    private double borderRadius;
    private final int[] elevationMilestones = new int[] {-32, -16, 0, 16, 64, 128};
    public RisingEdgeMinigame() {
        super(Text.translatable("ddv.minigame.rising_edge"), "0.0.1", SemanticVersioningSuffix.TEST);
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
        settings.add(new Setting(600.0, WARMUP_SECONDS));
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

        Collection<PlayerEntity> roledPlayers = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(PLAYER));
        Collection<PlayerEntity> roledSpectators = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(SPECTATOR));

        this.previousBorderSize = world.getWorldBorder().getSize();
        this.previousCenter = new Vec2f((float)world.getWorldBorder().getCenterX(), (float)world.getWorldBorder().getCenterZ());
        this.previousRules = world.getGameRules().copy();

        this.voidLevel = world.getBottomY() - 2;
        this.borderRadius = parseAsDouble(BORDER_DIST) / 2;

        this.center = new Vec2f((float) executor.getX(), (float) executor.getY());

        world.getWorldBorder().setCenter(Math.round(executor.getX()), Math.round(executor.getZ()));
        world.getWorldBorder().setSize(borderDist);

        players.forEach(player -> player.teleport(executor.getX(), executor.getY(), executor.getZ()));

        // Spectators in Spectator Mode
        roledSpectators.forEach(player -> SharedUtil.changePlayerGamemode(player, GameMode.SPECTATOR));

        if (glowing) { // Glowing to players, if enabled
            roledPlayers.forEach(player -> player.addStatusEffect(GLOWING_EFFECT_INSTANCE));
        }

        // Clear all players inventories
        roledPlayers.forEach(player -> player.getInventory().clear());
    }

    @Override
    public void onEnd(Collection<PlayerEntity> players, World world) {
        world.getWorldBorder().setCenter(this.previousCenter.x, this.previousCenter.y);
        world.getWorldBorder().setSize(this.previousBorderSize);
        world.getGameRules().setAllValues(this.previousRules, world.getServer());
    }

    @Override
    public boolean canWin(PlayerEntity winnerCandidate, Collection<PlayerEntity> players) {
        Collection<PlayerEntity> cpe = DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(PLAYER));
        cpe.remove(winnerCandidate);
        return cpe.isEmpty();
    }

    @Override
    public void onWin(PlayerEntity winningPlayer, World world, Collection<PlayerEntity> players) {
        players.forEach(player -> {
            player.sendMessage(Text.translatable("ddv.minigame.rising_edge.win_title", winningPlayer.getGameProfile().getName()), true);
        });

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
        int warmupLength = parseAsInt(WARMUP_SECONDS) * 20;
        int ascensionInterval = parseAsInt(RISE_INTERVAL);

        if (this.getTicks() % 20 == 0) { // For-each Second Recursion Loop
            if (this.getTicks() / 20 == warmupLength) {
                DDVGamesMod.gameManager.switchState(GameState.fromName(PVP), world);
            }
        }

        if (this.getTicks() % ascensionInterval == 0) {
            voidLevel += 1.0;
            voidRise(world, DDVGamesMod.gameManager.getPlayers());
        }

        killFallingGravityBlocksBeneathVoid(world);
    }

    @Override
    public boolean canStart(Collection<PlayerEntity> players) {
        DDVGamesMod.gameManager.getPlayersWithRole(Role.fromName(PLAYER));
        return true;
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

            DDVGamesMod.gameManager.getPlayers().forEach(player -> player.sendMessage(Text.translatable("ddv.minigame.rising_edge.warmup_start")));

        } else if (state.getName().matches(PVP)) {
            GameRules pvp = new GameRules();
            pvp.get(InitRules.PVP).set(true, world.getServer());
            pvp.get(GameRules.NATURAL_REGENERATION).set(parseAsBoolean(HEALING), world.getServer());

            this.enablePvp(DDVGamesMod.gameManager.getPlayers());
        }
    }

    @Override
    public void onStateEnds(GameState state, World world) {
    }

    private void voidRise(World world, Collection<PlayerEntity> players) {
        // Block Deletion
        Quadrilateral quad = SharedUtil.createQuad(this.borderRadius, this.center);
        BlockPos.iterate(new BlockPos(SharedUtil.xzVec2fToVec3d(quad.getCornerA(), (float) this.voidLevel)),
                        new BlockPos(SharedUtil.xzVec2fToVec3d(quad.getCornerB(), (float) this.voidLevel)))
                .iterator().forEachRemaining(blockPos -> {
                    if (!world.getBlockState(blockPos).isAir()) {
                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                    }
                });

        // Play Sound
        world.playSound(
                this.center.x, this.voidLevel, this.center.y,
                SoundEvents.ENTITY_EVOKER_FANGS_ATTACK,
                SoundCategory.NEUTRAL,
                20.0f,
                1.0f,
                true
        );

        // Inform Players
        if (Arrays.stream(this.elevationMilestones).anyMatch(value -> value == this.voidLevel)) {
            players.forEach(player -> player.sendMessage(Text.translatable("ddv.minigame.rising_edge.rise_milestone", this.voidLevel)));
        } else {
            players.forEach(player -> player.sendMessage(Text.translatable("ddv.minigame.rising_edge.rise")));
        }


        // I removed the particle effect from the datapack version, it probably would cause too much lag on top of the mass block replacing
    }

    private void killFallingGravityBlocksBeneathVoid(World world) {
        Quadrilateral rise = SharedUtil.createQuad(borderRadius, this.center);
        List<FallingBlockEntity> entities = world.getEntitiesByClass(FallingBlockEntity.class,
                SharedUtil.getBoxFromQuad(rise, world.getBottomY(), this.voidLevel), entity -> true);
        entities.forEach(Entity::kill);
    }

    private void enablePvp(Collection<PlayerEntity> players) {
        players.forEach(player -> {
            player.sendMessage(Text.translatable("ddv.minigame.rising_edge.pvp_enabled"), true);
            player.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 10.0f, 0.5f);
        });
    }
}
