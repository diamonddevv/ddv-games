package net.diamonddev.ddvgames.resource;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.libgenetics.common.api.v1.dataloader.cognition.CognitionResourceType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class SettingSetResourceType implements CognitionResourceType {

    public static final String NAME = "name";
    public static final String AUTHOR = "author";
    public static final String ID = "minigame_id";
    public static final String KEYS = "keys";

    public static final String KEY = "key";
    public static final String VALUE = "value";

    @Override
    public Identifier getId() {
        return DDVGamesMod.id("settingset");
    }

    @Override
    public void addJsonKeys(ArrayList<String> keys) {
        keys.add(NAME);
        keys.add(AUTHOR);
        keys.add(ID);
        keys.add(KEYS);
    }
}
