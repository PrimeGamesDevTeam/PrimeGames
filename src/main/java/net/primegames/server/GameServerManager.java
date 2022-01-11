package net.primegames.server;

import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.gameserver.MySQLCleanDeadServerData;
import net.primegames.providor.task.gameserver.MysqlDeleteServerData;
import net.primegames.providor.task.gameserver.MysqlSendServerDataTask;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class GameServerManager {

    @Getter
    private final GameServerSettings settings;
    private final MySqlProvider provider;
    @Getter
    private final HashMap<String, GameServer> servers = new HashMap<>();

    public GameServerManager(GameServerSettings settings) {
        this.settings = settings;
        provider = PrimeGames.getInstance().getMySQLprovider();
    }


    private void initSchedulers(){
        BukkitScheduler scheduler = PrimeGames.getInstance().getPlugin().getServer().getScheduler();
        //send server data to database
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MysqlSendServerDataTask(this.getSettings())), 0, 200);
        //get Server Data

        //delete garbage data
        scheduler.scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> provider.scheduleTask(new MySQLCleanDeadServerData()), 0, 1200);

        //set on disable Hook
        PrimeGames.getInstance().getPlugin().onDisableHook(() -> provider.scheduleTask(new MysqlDeleteServerData(settings.getIdentifier())));
    }

    public void addServer(String identifier, String gameModeId, String address, int port, int playerAmount, int capacity, String software,  String imageUrl, int statusCode) {
        GameServer server = new GameServer(identifier, GameMode.valueOf(gameModeId), address, port, playerAmount, capacity, software, imageUrl, GameServerStatus.getById(statusCode));
        servers.put(identifier, server);
    }

}
