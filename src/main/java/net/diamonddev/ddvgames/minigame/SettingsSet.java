package net.diamonddev.ddvgames.minigame;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsSet {
    private final Identifier id;
    private final HashMap<String, Double> keys;
    private final SettingsSetJsonFormat json;

    public SettingsSet(Identifier minigame, HashMap<String, Double> keys, SettingsSetJsonFormat originalJson) {
        this.id = minigame;
        this.keys = keys;

        this.json = originalJson;
    }

    public Identifier getId() {
        return id;
    }

    public HashMap<String, Double> getKeys() {
        return keys;
    }

    public SettingsSetJsonFormat getJson() {
        return json;
    }

    public boolean hasNameData() {
        return json.setname != null;
    }

    public boolean hasAuthorData() {
        return json.setauthor != null;
    }

    public String getSetName() {
        return json.setname;
    }
    public String getSetAuthor() {
        return json.setauthor;
    }

    public static SettingsSet fromJsonFormat(SettingsSetJsonFormat json) {

        final HashMap<String, Double> keyValues = new HashMap<>();
        json.settingkeys.forEach(jsonSetting -> keyValues.put(jsonSetting.key, jsonSetting.value));

        return new SettingsSet(new Identifier(json.id), keyValues, json);
    }


    private static final String JSON_SETNAME = "name";
    private static final String JSON_AUTHOR = "author";

    private static final String JSON_MINIGAMEID = "minigame_id";
    private static final String JSON_KEYS = "keys";

    public static class SettingsSetJsonFormat {

        @SerializedName(JSON_SETNAME)
        String setname;

        @SerializedName(JSON_AUTHOR)
        String setauthor;

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
