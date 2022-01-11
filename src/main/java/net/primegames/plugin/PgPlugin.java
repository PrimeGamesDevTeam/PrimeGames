package net.primegames.plugin;

import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.server.GameServerSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

abstract public class PgPlugin extends JavaPlugin {

    @Getter
    private boolean disabling = false;
    @Getter
    private boolean enabling = false;

    private final ArrayList<Runnable> disableHooks = new ArrayList<>();

    @Override
    final public void onEnable() {
        enabling = true;
        getJavaCore().onEnable();
        enable();
        enabling = false;
    }

    @Override
    final public void onDisable() {
        this.disabling = true;
        //run onDisable Hooks
        for (Runnable runnable : disableHooks) {
            runnable.run();
        }
        disable();
        getJavaCore().onDisable();
        disabling = false;
    }

    public void onDisableHook(Runnable runnable) {
        disableHooks.add(runnable);
    }

    public abstract PrimeGames getJavaCore();
    public abstract GameServerSettings getServerSettings();
    protected abstract void disable();
    protected abstract void enable();

}
