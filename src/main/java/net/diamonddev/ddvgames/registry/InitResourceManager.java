package net.diamonddev.ddvgames.registry;

import com.google.gson.Gson;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.SettingsSet;
import net.diamonddev.ddvgames.resource.SettingSetResourceListener;
import net.diamonddev.ddvgames.resource.SettingSetResourceType;
import net.diamonddev.libgenetics.common.api.v1.dataloader.cognition.CognitionDataListener;
import net.diamonddev.libgenetics.common.api.v1.dataloader.cognition.CognitionDataResource;
import net.diamonddev.libgenetics.common.api.v1.dataloader.cognition.CognitionResourceType;
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


    public static SettingSetResourceListener SETTINGSET_LISTENER = new SettingSetResourceListener();
    public static SettingSetResourceType SETTINGSET_TYPE = new SettingSetResourceType();
    @Override
    public void register() {
        CognitionDataListener.registerListener(SETTINGSET_LISTENER);
        SETTINGSET_LISTENER.getManager().registerType(SETTINGSET_TYPE);
    }

}
