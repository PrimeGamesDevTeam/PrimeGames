package net.primegames.components.store.listener;

import net.primegames.components.store.StoreComponent;
import net.primegames.components.store.task.PlayerPurchaseCheckTask;
import net.primegames.event.player.BedrockPlayerLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;

public class StoreListener implements Listener {

    @EventHandler
    public void onLoaded(BedrockPlayerLoadedEvent event) throws SQLException {
        StoreComponent.getInstance().getMySqlProvider().scheduleTask(new PlayerPurchaseCheckTask(event.getPlayerData()));
    }

}
