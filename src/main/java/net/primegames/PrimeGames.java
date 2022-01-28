package net.primegames;

import com.earth2me.essentials.Essentials;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.primegames.commands.BedrockPlayerCommandHandler;
import net.primegames.leaderboard.LeaderboardManager;
import net.primegames.listener.ChunkSpawnerLimitListener;
import net.primegames.listener.CorePlayerListener;
import net.primegames.player.CorePlayerManager;
import net.primegames.plugin.PrimePlugin;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.MySQLInitialCoreTask;
import net.primegames.server.GameServer;
import net.primegames.server.GameServerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getLogger;
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
    private FloodgateApi floodgateApi;
    @Getter
    private Essentials essentials;
    @Getter private Economy economy;

    public PrimeGames(PrimePlugin plugin) {
        this.plugin =  plugin;
        instance = this;
        this.mySQLprovider = new MySqlProvider();
        this.corePlayerManager = new CorePlayerManager();
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

    public boolean attemptEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            return false;
        }
        LeaderboardManager.init();
        this.floodgateApi = FloodgateApi.getInstance();
        Essentials essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
        if (essentials != null) {
            this.essentials = essentials;
        } else {
            throw new RuntimeException("Essentials plugin was not found!");
        }
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", plugin.getDescription().getName()));
            getServer().getPluginManager().disablePlugin(plugin);
        }
        mySQLprovider.scheduleTask(new MySQLInitialCoreTask());
        registerCoreListeners();
        gameServerManager = new GameServerManager(plugin);
        return true;
    }

    public void disable() {
        //close mysql connection
        try {
            mySQLprovider.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerCoreListeners() {
        @NotNull PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new CorePlayerListener(), plugin);
        manager.registerEvents(new ChunkSpawnerLimitListener(), plugin);
        manager.registerEvents(new BedrockPlayerCommandHandler(), plugin);
    }

}
