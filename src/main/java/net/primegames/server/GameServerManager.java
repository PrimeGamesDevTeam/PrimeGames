package net.primegames.server;

import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.plugin.PrimePlugin;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.server.MySQLCleanDeadServerData;
import net.primegames.providor.task.server.MySQLReceiveServerData;
import net.primegames.providor.task.server.MysqlSendServerDataTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class GameServerManager {

    @Getter
    private final GameServerSettings settings;
    @Getter
    private final HashMap<String, GameServer> servers = new HashMap<>();

    public GameServerManager(PrimePlugin plugin) {
        settings = plugin.getServerSettings().getServerSettings();
        initSchedulers(PrimeGames.getInstance().getMySQLprovider());
    }

    private void initSchedulers(MySqlProvider provider) {
        LoggerUtils.info("Initializing server schedulers");
        BukkitScheduler scheduler = PrimeGames.getInstance().getPlugin().getServer().getScheduler();
        //send server data to database
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MysqlSendServerDataTask(this.getSettings())), 0, 200);
        //get servers Data
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MySQLReceiveServerData()), 0, 200);

        //delete garbage data
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MySQLCleanDeadServerData()), 0, 1200);
    }

    public void addServer(String identifier, String gameModeId, String address, int port, int playerAmount, int capacity, String software, String imageUrl, int statusCode) {
        GameServer server = new GameServer(identifier, GameMode.valueOf(gameModeId.toUpperCase()), address, port, playerAmount, capacity, software, imageUrl, GameServerStatus.getById(statusCode));
        servers.put(identifier, server);
    }

}
