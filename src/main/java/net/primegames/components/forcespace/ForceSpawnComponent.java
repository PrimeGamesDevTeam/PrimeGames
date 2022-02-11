package net.primegames.components.forcespace;

import lombok.NonNull;
import net.primegames.components.Component;
import net.primegames.plugin.PrimePlugin;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ForceSpawnComponent implements Component, Listener {

    private final Location spawnLocation;

    public ForceSpawnComponent(PrimePlugin plugin, Location spawnLocation){
        this.spawnLocation = spawnLocation;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public @NonNull String getIdentifier() {
        return "forcespawn.component";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event){
        event.getPlayer().teleport(spawnLocation);
    }
}
