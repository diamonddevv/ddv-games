package net.diamonddev.ddvgames.minigame;


import net.diamonddev.ddvgames.util.SemanticVersioningSuffix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Minigame {

    private final MutableText name;
    private final String version;
    private final SemanticVersioningSuffix verSuffix;
    private ArrayList<Role> roles;
    private ArrayList<Setting> settings;
    private PlayerEntity winner;
    private boolean running;

    protected Minigame(MutableText name, String semanticVersion, SemanticVersioningSuffix versioningSuffix) {
        this.running = false;
        this.name = name;

        this.winner = null;

        this.settings = new ArrayList<>();
        this.roles = new ArrayList<>();
        this.settings = addSettings(this.settings);
        this.roles = addRoles(this.roles);

        this.version = semanticVersion;
        this.verSuffix = versioningSuffix;
    }

    public boolean isRunning() {
        return running;
    }

    public abstract ArrayList<Role> addRoles(ArrayList<Role> roles);
    public abstract ArrayList<Setting> addSettings(ArrayList<Setting> settings);

    public abstract void onStart(Entity executor, Collection<PlayerEntity> players, World world);
    public abstract void onEnd(Collection<PlayerEntity> players, World world);

    public ArrayList<Setting> getSettings() {return this.settings;}
    public ArrayList<Role> getRoles() {return this.roles;}
    public boolean canStart(Collection<PlayerEntity> players) {
        return players.size() > 1;
    }

    public void start(Entity executor, Collection<PlayerEntity> players, World world) {
        if (this.canStart(players)) {
            this.winner = null;
            this.running = true;
            this.onStart(executor, players, world);
        }
    }
    public void end(Collection<PlayerEntity> players, World world) {
        if (this.isRunning()) {
            this.running = false;
            this.onEnd(players, world);
        }
    }


    public Text getName() {
        return this.name;
    }
    public Text getSemanticVersion() {
        MutableText text = Text.translatable("ddv.semanticVersioning.prefix");
        text.append(Text.literal(this.version));
        text.append(Text.translatable(this.verSuffix.getTranslationKey()));

        return text;
    }

}
