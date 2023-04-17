package net.diamonddev.ddvgames.resource;

import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.SettingsSet;
import net.diamonddev.libgenetics.common.api.v1.dataloader.cognition.CognitionDataListener;
import net.diamonddev.libgenetics.common.api.v1.dataloader.cognition.CognitionDataResource;
import net.minecraft.util.Identifier;

import java.util.HashMap;


public class SettingSetResourceListener extends CognitionDataListener {

    public static HashMap<String, SettingsSet> RESOURCE_SETTINGSSET = new HashMap<>();
    public SettingSetResourceListener() {
        super("SMES SettingSets", DDVGamesMod.id("settingset_listener"), "settingsets");
    }

    @Override
    public void onReloadForEachResource(CognitionDataResource resource, Identifier path) {
        SettingsSet.SettingsSetJsonFormat format = resource.getAsClass(SettingsSet.SettingsSetJsonFormat.class);
        SettingsSet set = SettingsSet.fromJsonFormat(format);

        StringBuilder filepath = new StringBuilder(path.getPath()).delete(0, 12).reverse().delete(0, 5).reverse(); // Get rid of prefixed initial filepath and .json
        filepath.insert(0, path.getNamespace() + ":"); // Prepend "<namespace>:"

        RESOURCE_SETTINGSSET.put(path.toString(), set);
    }
}
