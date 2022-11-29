package net.diamonddev.ddvgames.registry;

import com.google.gson.Gson;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.SettingsSet;
import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class InitResourceManager implements RegistryInitializer {

    private static final Logger RESOURCE_MANAGER_LOGGER = LoggerFactory.getLogger("DDV Minigames  - Resource Manager");
    private static final Gson GSON_READER = new Gson();


    public static HashMap<String, SettingsSet> RESOURCE_SETTINGSSET = new HashMap<>();


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

                        if (FabricLoaderImpl.INSTANCE.isDevelopmentEnvironment()) {
                            RESOURCE_MANAGER_LOGGER.info("-- DevEnv SettingSet Resource Loading Info --");

                            RESOURCE_MANAGER_LOGGER.info("Loaded File: " + id.toString());

                            RESOURCE_MANAGER_LOGGER.info("Has Name: " + set.hasNameData());
                            RESOURCE_MANAGER_LOGGER.info("Has Author: " + set.hasAuthorData());

                            RESOURCE_MANAGER_LOGGER.info("Read Name: " + set.getSetName());
                            RESOURCE_MANAGER_LOGGER.info("Read Name: " + set.getSetAuthor());

                            RESOURCE_MANAGER_LOGGER.info("Read Game ID: " + set.getId());
                            RESOURCE_MANAGER_LOGGER.info("Read Keys: " + set.getKeys());

                            RESOURCE_MANAGER_LOGGER.info("---------------------------------------------");
                        }

                        StringBuilder path = new StringBuilder(id.getPath()).delete(0, 12).reverse().delete(0, 5).reverse(); // Get rid of prefixed initial filepath and .json
                        path.insert(0, id.getNamespace() + ":"); // Prepend "<namespace>:"

                        RESOURCE_SETTINGSSET.put(path.toString(), set);
                    } catch (Exception e) {
                        RESOURCE_MANAGER_LOGGER.error("Error occurred while loading SettingsSet resource json " + id.toString(), e);
                    }
                }
            }
        }
    }

}
