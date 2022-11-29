package net.diamonddev.ddvgames.registry;

import net.diamonddev.libgenetics.common.api.v1.interfaces.RegistryInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

import static net.minecraft.world.GameRules.*;

public class InitRules implements RegistryInitializer {

    public static Key<BooleanRule> PVP;


    @Override
    public void register() {
        PVP = buildBoolean("ddvg:pvp", true);
    }

    private static Key<BooleanRule> buildBoolean(String name, boolean defaultValue) {
        if (!GameRuleRegistry.hasRegistration(name)) {
            return GameRuleRegistry.register(name, Category.MISC, GameRuleFactory.createBooleanRule(defaultValue));
        }
        return null;
    }
}
