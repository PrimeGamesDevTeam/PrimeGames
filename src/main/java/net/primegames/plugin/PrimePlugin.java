package net.primegames.plugin;

import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.server.GameServerSettings;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

abstract public class PrimePlugin extends JavaPlugin {

    @Getter
    private boolean disabling = false;
    @Getter
    private boolean enabling = false;
    @Getter
    private PrimeGames primeGames;
    private final ArrayList<Runnable> disableHooks = new ArrayList<>();

    public PrimePlugin(@NotNull final JavaPluginLoader loader, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file){
        super(loader, description, dataFolder, file);
        this.primeGames = new PrimeGames(this);
    }

    public PrimePlugin(){
    }

    @Override
    final public void onLoad() {
       this.onInternalLoad();
    }

    @Override
    final public void onEnable() {
        enabling = true;
        primeGames.onInternalEnable();
        onInternalEnable();
        enabling = false;
    }

    @Override
    final public void onDisable() {
        this.disabling = true;
        //run onDisable Hooks
        for (Runnable runnable : disableHooks) {
            runnable.run();
        }
        onInternalDisable();
        primeGames.onDisable();
        disabling = false;
    }

    public void onDisableHook(Runnable runnable) {
        disableHooks.add(runnable);
    }

    public abstract GameServerSettings getServerSettings();
    protected void onInternalDisable(){}
    protected void onInternalEnable(){}
    protected void onInternalLoad(){}

}
