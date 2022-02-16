package net.primegames;

import com.earth2me.essentials.Essentials;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.primegames.commands.BedrockPlayerCommandHandler;
import net.primegames.listener.BedrockPlayerListener;
import net.primegames.player.BedrockPlayerManager;
import net.primegames.plugin.PrimePlugin;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.MysqlCredentials;
import net.primegames.providor.connection.ConnectionId;
import net.primegames.providor.task.MySQLInitialCoreTask;
import net.primegames.server.GameServer;
import net.primegames.server.GameServerManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public final class PrimeGames {

    @Getter
    private static PrimeGames instance;
    @Getter
    private final MySqlProvider mySQLprovider;
    @Getter
    private final BedrockPlayerManager corePlayerManager;
    @Getter
    private final PrimePlugin plugin;

    @Getter
    @Setter
    private GameServer gameServer;
    @Getter
    private GameServerManager gameServerManager;
    @Getter
    private FloodgateApi floodgateApi;
    @Getter
    private Economy economy;
    @Getter
    private Essentials essentials;

    public PrimeGames(PrimePlugin plugin) {
        this.plugin = plugin;
        instance = this;
        this.mySQLprovider = new MySqlProvider(plugin);
        mySQLprovider.createConnection(ConnectionId.CORE, MysqlCredentials.getCredentials(plugin, "core"));
        this.corePlayerManager = new BedrockPlayerManager();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private boolean setupEssentials(){
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            return false;
        }
        essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        return true;
    }

    public boolean attemptEnable() throws SQLException {
        this.floodgateApi = FloodgateApi.getInstance();
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", plugin.getDescription().getName()));
            getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }
        if (!setupEssentials()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Essentials dependency found!", plugin.getDescription().getName()));
            getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }
        mySQLprovider.scheduleTask(new MySQLInitialCoreTask(mySQLprovider.getConnection(ConnectionId.CORE)));
        registerCoreListeners();
        gameServerManager = new GameServerManager(plugin);
        return true;
    }

    public void disable() {
        //close mysql connection
        mySQLprovider.getConnections().forEach((ConnectionId connectionId, Connection connection) -> {
            try {
                connection.close();
                mySQLprovider.remove(connectionId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void registerCoreListeners() {
        @NotNull PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BedrockPlayerListener(), plugin);
        manager.registerEvents(new BedrockPlayerCommandHandler(), plugin);
    }

    public static PrimePlugin plugin(){
        if (!instance.getPlugin().isEnabled()){
            throw new IllegalStateException("PrimePlugin is not enabled!");
        }
        return instance.getPlugin();
    }

    public static Connection defaultConnection() throws SQLException {
        return instance.getMySQLprovider().getConnection(ConnectionId.CORE);
    }
}
