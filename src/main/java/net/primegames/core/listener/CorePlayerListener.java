package net.primegames.core.listener;

import net.primegames.core.PrimesCore;
import net.primegames.core.player.CorePlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CorePlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        PrimesCore.getInstance().getCorePlayerManager().initPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        CorePlayerManager.getInstance().remove(event.getPlayer());
    }

}
