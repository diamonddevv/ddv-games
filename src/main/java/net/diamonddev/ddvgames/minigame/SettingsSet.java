package net.diamonddev.ddvgames.minigame;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsSet {
    private final Identifier id;
    private final HashMap<String, Double> keys;

    public SettingsSet(Identifier minigame, HashMap<String, Double> keys) {
        this.id = minigame;
        this.keys = keys;
    }

    public Identifier getId() {
        return id;
    }

    public HashMap<String, Double> getKeys() {
        return keys;
    }

    public static SettingsSet fromJsonFormat(SettingsSetJsonFormat json) {

        final HashMap<String, Double> keyValues = new HashMap<>();
        json.settingkeys.forEach(jsonSetting -> keyValues.put(jsonSetting.key, jsonSetting.value));

        return new SettingsSet(new Identifier(json.id), keyValues);
    }


    private static final String JSON_MINIGAMEID = "minigame_id";
    private static final String JSON_KEYS = "keys";

    public static class SettingsSetJsonFormat {
        @SerializedName(JSON_MINIGAMEID)
        String id;

        @SerializedName(JSON_KEYS)
        ArrayList<JsonSetting> settingkeys;

        public static class JsonSetting {
            String key;
            double value;
        }
    }
}
