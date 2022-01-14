package net.primegames.server;

import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.plugin.PrimePlugin;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.gameserver.MySQLCleanDeadServerData;
import net.primegames.providor.task.gameserver.MySQLReceiveServerData;
import net.primegames.providor.task.gameserver.MysqlDeleteServerData;
import net.primegames.providor.task.gameserver.MysqlSendServerDataTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class GameServerManager {

    @Getter
    private GameServerSettings settings;
    private final MySqlProvider provider;
    @Getter
    private final HashMap<String, GameServer> servers = new HashMap<>();

    public GameServerManager(PrimePlugin plugin) {
        initSettings(plugin);
        provider = PrimeGames.getInstance().getMySQLprovider();
    }

    private void initSettings(PrimePlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        GameMode gameMode;
        GameServerStatus status;
        if (checkConfig(plugin)) {
            try {
                gameMode = GameMode.valueOf(config.getString("core.server.ganmemode"));
            } catch (IllegalArgumentException e) {
                LoggerUtils.error("GameMode not found. GameMode List: " + GameMode.getGameModeList());
                plugin.getServer().shutdown();
                return;
            }
            try {
                status = GameServerStatus.valueOf(config.getString("core.server.status"));
            } catch (IllegalArgumentException e) {
                LoggerUtils.error("GameServerStatus not found. GameServerStatus List: " + GameServerStatus.getGameServerStatusList());
                plugin.getServer().shutdown();
                return;
            }
        } else {
            return;
        }
        settings = new GameServerSettings(config.getString("core.server.identifier"), gameMode, status, config.getString("core.server.address"), config.getInt("core.server.port"), config.getString("core.server.icon"));
        initSchedulers();
    }

    private boolean checkConfig(PrimePlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        if (config.get("core.server.identifier") == null || config.get("core.server.ganmemode") == null || config.get("core.server.status") == null || config.get("core.server.address") == null || config.get("core.server.port") == null) {
            LoggerUtils.warn("Server settings not found, creating default settings");
            config.set("core.server.identifier", "default");
            config.set("core.server.ganmemode", "");
            config.set("core.server.status", GameServerStatus.ALPHA.toString());
            config.set("core.server.address", "primegames.net");
            config.set("core.server.port", 19132);
            config.set("core.server.icon", "https://primegames.net/assets/img/logo.png");
            plugin.saveConfig();
            LoggerUtils.warn("Server settings created, set correct details and restart server please restart the server");
            plugin.getServer().shutdown();
            return false;
        }
        return true;
    }


    private void initSchedulers(){
        LoggerUtils.info("Initializing server schedulers");
        BukkitScheduler scheduler = PrimeGames.getInstance().getPlugin().getServer().getScheduler();
        //send server data to database
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MysqlSendServerDataTask(this.getSettings())), 0, 200);
        //get servers Data
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MySQLReceiveServerData()), 0, 200);

        //delete garbage data
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MySQLCleanDeadServerData()), 0, 1200);
    }

    public void addServer(String identifier, String gameModeId, String address, int port, int playerAmount, int capacity, String software,  String imageUrl, int statusCode) {
        GameServer server = new GameServer(identifier, GameMode.valueOf(gameModeId.toUpperCase()), address, port, playerAmount, capacity, software, imageUrl, GameServerStatus.getById(statusCode));
        servers.put(identifier, server);
    }

}
