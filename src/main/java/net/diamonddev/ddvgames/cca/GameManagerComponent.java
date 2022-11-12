package net.diamonddev.ddvgames.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.diamonddev.ddvgames.DDVGamesMod;
import net.diamonddev.ddvgames.minigame.GameManager;
import net.diamonddev.ddvgames.registry.InitRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;

public class GameManagerComponent implements AutoSyncedComponent {

    private GameManager gameManager = DDVGamesMod.gameManager;

    private final String gameManagerCompoundComponent = "GameManager";
    private final String gameComponent = "Minigame";
    private final String playerListComponent = "Players";
    private final String settingsComponent = "Settings";
    private final String rolesComponent = "PlayerRoles";

    @Override
    public void readFromNbt(NbtCompound tag) {
        // Game
        tag.getCompound(gameManagerCompoundComponent).getString(gameComponent);

        // Players
        tag.getCompound(gameManagerCompoundComponent).getList(playerListComponent, NbtElement.LIST_TYPE);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        // Game
        tag.getCompound(gameManagerCompoundComponent).putString(gameComponent, gameManager.hasGame() ? InitRegistries.MINIGAMES.getId(gameManager.getGame()).toString() : "");

        // Players
        NbtList playerList = new NbtList();
        for (PlayerEntity player : gameManager.getPlayers()) {
            NbtCompound thisCompound = new NbtCompound();
            NbtHelper.writeGameProfile(thisCompound, player.getGameProfile());
            playerList.add(thisCompound);
        }
        tag.getCompound(gameManagerCompoundComponent).put(playerListComponent, playerList);
    }
}
