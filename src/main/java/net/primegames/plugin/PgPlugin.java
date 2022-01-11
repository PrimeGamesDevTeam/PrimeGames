package net.primegames.plugin;

import lombok.Getter;
import net.primegames.PrimeGames;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

abstract public class PgPlugin extends JavaPlugin {

    @Getter
    private boolean disabling = false;
    @Getter
    private boolean enabling = false;

    private final ArrayList<Runnable> disableHooks = new ArrayList<>();

    public abstract PrimeGames getJavaCore();

    @Override
    public void onEnable() {
        enabling = true;
        getJavaCore().onEnable();
        enable();
        enabling = false;
    }

    @Override
    public void onDisable() {
        this.disabling = true;
        //run onDisable Hooks
        for (Runnable runnable : disableHooks) {
            runnable.run();
        }
        disable();
        getJavaCore().onDisable();
        disabling = false;
    }

    protected void disable() {
    }

    private void enable() {
    }

    public void onDisableHook(Runnable runnable) {
        disableHooks.add(runnable);
    }

}
