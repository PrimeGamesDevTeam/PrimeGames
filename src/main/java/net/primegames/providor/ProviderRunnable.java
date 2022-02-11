package net.primegames.providor;

import net.primegames.plugin.PrimePlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ProviderRunnable extends BukkitRunnable {


    protected PrimePlugin verifyPluginEnabled(PrimePlugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalStateException("Plugin is not enabled!");
        }
        return plugin;
    }
}
