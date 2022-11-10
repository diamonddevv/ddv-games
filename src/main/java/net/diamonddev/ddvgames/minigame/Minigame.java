package net.diamonddev.ddvgames.minigame;


import net.diamonddev.ddvgames.minigame.setting.Setting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Minigame {

    private final MutableText name;
    private ArrayList<Setting> settings;
    private PlayerEntity winner;
    private boolean running;

    protected Minigame(MutableText name) {
        this.running = false;
        this.name = name;
        this.winner = null;
        this.settings = new ArrayList<>();
        this.settings = addSettings(this.settings);
    }

    public boolean isRunning() {
        return running;
    }

    public abstract ArrayList<Setting> addSettings(ArrayList<Setting> settings);

    public ArrayList<Setting> getSettings() {return this.settings;}
    public boolean canStart(Collection<PlayerEntity> players) {
        return players.size() > 1;
    }

    public void start(Collection<PlayerEntity> players) {
        if (this.canStart(players)) {
            this.winner = null;
            this.running = true;
        }
    }
    public void end() {
        if (this.isRunning()) {
            this.running = false;
        }
    }


    public Text getName() {
        return this.name;
    }

}
