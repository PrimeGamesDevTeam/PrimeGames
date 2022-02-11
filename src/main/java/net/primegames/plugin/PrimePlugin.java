package net.primegames.plugin;

import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.components.ComponentManager;
import net.primegames.components.vote.VoteComponent;
import net.primegames.server.settings.ServerSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract public class PrimePlugin extends JavaPlugin {

    @Getter
    private final PrimeGames primeGames;
    private final ArrayList<Runnable> disableHooks = new ArrayList<>();
    @Getter
    private boolean disabling = false;
    @Getter
    private boolean enabling = false;
    @Getter
    private ServerSettings serverSettings;

    public PrimePlugin(@NotNull final JavaPluginLoader loader, @NotNull final PluginDescriptionFile description, @NotNull final File dataFolder, @NotNull final File file) {
        super(loader, description, dataFolder, file);
        this.primeGames = new PrimeGames(this);
    }

    public PrimePlugin() {
        this.primeGames = new PrimeGames(this);
    }

    @Override
    final public void onLoad() {
        this.onInternalLoad();
    }

    @Override
    final public void onEnable() {
        enabling = true;
        setEnabled(primeGames.attemptEnable());
        onInternalEnable();
        registerListeners(getServer().getPluginManager());
        registerComponents(ComponentManager.getInstance());
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

    protected void registerCommand(Command command, boolean force) {
        if (!force) {
            Command rCommand = Bukkit.getCommandMap().getCommand(command.getName());
            if (rCommand != null) {
                rCommand.unregister(Bukkit.getCommandMap());
            }
            for (String alias : command.getAliases()) {
                Command rAlias = Bukkit.getCommandMap().getCommand(alias);
                if (rAlias != null) {
                    rAlias.unregister(Bukkit.getCommandMap());
                }
            }
        }
        registerCommand(command);
    }

    private void registerCommand(Command command) {
        Bukkit.getCommandMap().register(command.getName(), command);
    }

    protected abstract void registerListeners(PluginManager pluginManager);

    protected abstract void registerComponents(ComponentManager componentManager);

    public final void onDisableHook(Runnable runnable) {
        disableHooks.add(runnable);
    }

    protected void onInternalDisable() {
    }

    protected void onInternalEnable() {
    }

    protected void onInternalLoad() {
    }

    public <T extends ServerSettings> void setServerSettings(T serverSettings) {
        this.serverSettings = serverSettings;
    }
}
