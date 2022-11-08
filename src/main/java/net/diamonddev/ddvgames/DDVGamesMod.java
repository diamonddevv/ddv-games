package net.diamonddev.ddvgames;

import net.diamonddev.ddvgames.minigame.GameManager;
import net.diamonddev.ddvgames.registry.InitCommand;
import net.diamonddev.ddvgames.registry.InitMinigames;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.diamonddev.libgenetics.common.api.v1.util.IdentifierBuilder;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DDVGamesMod implements ModInitializer {

	public static final String modid = "ddvgames";
	public static final Logger LOGGER = LoggerFactory.getLogger("DDV Minigames");
	public static final IdentifierBuilder id = new IdentifierBuilder(modid);

	public static GameManager gameManager = GameManager.getGameManager();
	@Override
	public void onInitialize() {
		long start = System.currentTimeMillis();
		//
		new InitRegistries().register();
		new InitMinigames().register();

		new InitCommand().register();
		//
		long initTime = System.currentTimeMillis() - start;
		LOGGER.info("Mod " + modid + " initialized in " + initTime + " millisecond(s)!");
	}
}
