package net.primegames.providor.task;

import net.primegames.player.BedrockPlayerManager;
import net.primegames.plugin.PrimePlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public abstract class ProviderRunnable extends BukkitRunnable {


    protected void verifyPluginEnabled(PrimePlugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalStateException("Plugin is not enabled!");
        }
    }

    protected void verifyPlayer(UUID serverUUID, UUID bedrockUUID) {
        BedrockPlayerManager bedrockPlayerManager = BedrockPlayerManager.getInstance();
        if (!bedrockPlayerManager.getPlayerUUIDs().containsKey(serverUUID) || !bedrockPlayerManager.getPlayerUUIDs().get(serverUUID).equals(bedrockUUID)){
            throw new IllegalArgumentException("Player is not registered!");
        }
    }
}
