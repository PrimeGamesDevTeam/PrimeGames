package net.primegames.listener;

import net.primegames.PrimeGames;
import net.primegames.player.BedrockPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CorePlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerJoinEvent event) {
        PrimeGames.getInstance().getCorePlayerManager().initPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        BedrockPlayerManager.getInstance().remove(event.getPlayer());
    }

}
