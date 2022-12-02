package net.diamonddev.ddvgames.minigame;

import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public class SpeedTagMinigame extends Minigame {
    public SpeedTagMinigame() {
        super(Text.translatable("ddv.minigame.speed_tag"), "0.0.0", SemanticVersioningSuffix.TEST);
    }

    @Override
    public ArrayList<Role> addRoles(ArrayList<Role> roles) {
        return null;
    }

    @Override
    public ArrayList<Setting> addSettings(ArrayList<Setting> settings) {
        return null;
    }

    @Override
    public ArrayList<GameState> addGameStates(ArrayList<GameState> states) {
        return null;
    }

    @Override
    public void onStart(Entity executor, Collection<PlayerEntity> players, World world) {
        executor.sendMessage(Text.literal("..."));
        this.end(players, world);
    }

    @Override
    public void onEnd(Collection<PlayerEntity> players, World world) {

    }

    @Override
    public boolean canWin(PlayerEntity winnerCandidate, Collection<PlayerEntity> players) {
        return false;
    }

    @Override
    public void onWin(PlayerEntity winningPlayer, World world, Collection<PlayerEntity> players) {

    }

    @Override
    public void tickClock(World world) {

    }

    @Override
    public String getDefaultRoleName() {
        return null;
    }

    @Override
    public String getStartingStateName() {
        return null;
    }

    @Override
    public void onStateStarts(GameState state, World world) {

    }

    @Override
    public void onStateEnds(GameState state, World world) {

    }
}
