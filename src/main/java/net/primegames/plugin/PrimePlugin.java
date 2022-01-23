package net.primegames.plugin;

import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.server.GameServerSettings;
import net.primegames.server.settings.ServerSettings;
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
    private final PrimeGames primeGames;
    private final ArrayList<Runnable> disableHooks = new ArrayList<>();

    public PrimePlugin(@NotNull final JavaPluginLoader loader, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file){
        super(loader, description, dataFolder, file);
        this.primeGames = new PrimeGames(this);
    }

    public PrimePlugin(){
        this.primeGames = new PrimeGames(this);
    }

    @Override
    final public void onLoad() {
       this.onInternalLoad();
    }

    @Override
    final public void onEnable() {
        enabling = true;
        primeGames.enable();
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
        primeGames.disable();
        disabling = false;
    }

    public final void onDisableHook(Runnable runnable) {
        disableHooks.add(runnable);
    }

    protected void onInternalDisable(){}
    protected void onInternalEnable(){}
    protected void onInternalLoad(){}
    public abstract <T extends ServerSettings> T getSettings();

}
