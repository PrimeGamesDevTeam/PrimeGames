package net.primegames;

import lombok.Getter;
import net.primegames.listener.ChunkSpawnerListener;
import net.primegames.listener.CorePlayerListener;
import net.primegames.player.CorePlayerManager;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.MySQLInitialCoreTask;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public final class JavaCore {

    @Getter
    private static JavaCore instance;
    @Getter
    private final MySqlProvider mySQLprovider;
    @Getter
    private final CorePlayerManager corePlayerManager;
    @Getter
    private final JavaPlugin plugin;

    public JavaCore(JavaPlugin plugin) {
        this.plugin =  plugin;
        instance = this;
        this.mySQLprovider = new MySqlProvider();
        this.corePlayerManager = new CorePlayerManager();
    }

    public void onEnable() {
        mySQLprovider.scheduleTask(new MySQLInitialCoreTask());
        registerListeners();
    }

    public void onDisable() {
        //close mysql connection
        try {
            mySQLprovider.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        @NotNull PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new CorePlayerListener(), plugin);
        manager.registerEvents(new ChunkSpawnerListener(), plugin);
    }

}
