package net.diamonddev.ddvgames.registry;

import com.google.gson.Gson;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.SettingsSet;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class InitResourceManager implements RegistryInitializer {

    private static final Logger RESOURCE_MANAGER_LOGGER = LoggerFactory.getLogger("DDV Minigames  - Resource Manager");
    private static final Gson GSON_READER = new Gson();


    public static ArrayList<SettingsSet> RESOURCE_SETTINGSSET = new ArrayList<>();
    public static ArrayList<String> RESOURCE_SETTINGSSET_KEYS = new ArrayList<>();


    @Override
    public void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new GameSettingsReloadableData());
    }



    public static class GameSettingsReloadableData implements SimpleSynchronousResourceReloadListener {

        @Override
        public Identifier getFabricId() {
            return DDVGamesMod.id.build("game_settings");
        }

        @Override
        public void reload(ResourceManager manager) {
            // Invalidate Cached SettingSets
            RESOURCE_SETTINGSSET.clear();

            for(Identifier id : manager.findResources("settingsets", path -> path.getPath().endsWith(".json")).keySet()) {
                if (manager.getResource(id).isPresent()) {
                    try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                        // Consume stream
                        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8); // Create Reader
                        SettingsSet.SettingsSetJsonFormat formattedJson = GSON_READER.fromJson(reader, SettingsSet.SettingsSetJsonFormat.class);
                        SettingsSet set = SettingsSet.fromJsonFormat(formattedJson);
                        RESOURCE_SETTINGSSET.add(set);
                        RESOURCE_SETTINGSSET_KEYS.add(set.getId().toString());

                    } catch (Exception e) {
                        RESOURCE_MANAGER_LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
                    }
                }
            }
        }
    }

}
