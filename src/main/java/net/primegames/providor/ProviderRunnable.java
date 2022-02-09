package net.primegames.providor;

import net.primegames.plugin.PrimePlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ProviderRunnable extends BukkitRunnable {


    protected void verifyPluginEnabled(PrimePlugin... plugin) {
        for (PrimePlugin p : plugin) {
            if (!p.isEnabled()) {
                throw new IllegalStateException("Plugin " + p.getName() + " is not enabled!");
            }
        }
    }
}
