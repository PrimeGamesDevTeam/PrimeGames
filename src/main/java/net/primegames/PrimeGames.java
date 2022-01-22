package net.primegames;

import lombok.Getter;
import lombok.Setter;
import net.primegames.listener.ChunkSpawnerLimitListener;
import net.primegames.listener.CorePlayerListener;
import net.primegames.player.CorePlayerManager;
import net.primegames.plugin.PrimePlugin;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.MySQLInitialCoreTask;
import net.primegames.server.GameServer;
import net.primegames.server.GameServerManager;
import org.bukkit.plugin.PluginManager;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;

public final class PrimeGames {

    @Getter
    private static PrimeGames instance;
    @Getter
    private final MySqlProvider mySQLprovider;
    @Getter
    private final CorePlayerManager corePlayerManager;
    @Getter
    private final PrimePlugin plugin;

    @Getter @Setter
    private GameServer gameServer;
    @Getter
    private GameServerManager gameServerManager;
    @Getter
    private final FloodgateApi floodgateApi;

    public PrimeGames(PrimePlugin plugin) {
        this.plugin =  plugin;
        instance = this;
        this.mySQLprovider = new MySqlProvider();
        this.corePlayerManager = new CorePlayerManager();
        this.floodgateApi = FloodgateApi.getInstance();
    }

    public void enable() {
        mySQLprovider.scheduleTask(new MySQLInitialCoreTask());
        registerListeners();
        gameServerManager = new GameServerManager(plugin);
    }

    public void disable() {
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
        manager.registerEvents(new ChunkSpawnerLimitListener(), plugin);
    }

}
