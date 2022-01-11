package net.primegames.providor;

import net.primegames.plugin.PgPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ProviderRunnable extends BukkitRunnable {


    protected void verifyPluginEnabled(PgPlugin...plugin) {
        for(PgPlugin p : plugin) {
            if(!p.isEnabled()) {
                throw new IllegalStateException("Plugin " + p.getName() + " is not enabled!");
            }
        }
    }
}
