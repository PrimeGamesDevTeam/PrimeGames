package net.primegames.core.listener;

import net.primegames.core.player.CorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class CorePlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        UUID uuid = CorePlayer.getUuid(event.getPlayer());
        if(uuid != null) {
            CorePlayer.load(uuid, event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = CorePlayer.getUuid(event.getPlayer());
        if(uuid != null) {
            CorePlayer.remove(uuid);
        }
    }

}
