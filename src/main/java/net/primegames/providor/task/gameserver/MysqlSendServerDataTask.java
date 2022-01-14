package net.primegames.providor.task.gameserver;

import net.primegames.PrimeGames;
import net.primegames.providor.MySqlPostQueryTask;
import net.primegames.server.GameServerSettings;
import net.primegames.utils.LoggerUtils;
import org.bukkit.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlSendServerDataTask extends MySqlPostQueryTask {

    private final String identifier;
    private final String gameMode;
    private final int port;
    private final int playerAmount;
    private final int capacity;
    private final String software;
    private final String imageURL;
    private final String host;
    private final int status;
    

    public MysqlSendServerDataTask(GameServerSettings settings) {
        Server server = PrimeGames.getInstance().getPlugin().getServer();
        this.identifier = settings.getIdentifier();
        this.gameMode = settings.getGameMode().name();
        this.port = server.getPort();
        this.playerAmount = server.getOnlinePlayers().size();
        this.capacity = server.getMaxPlayers();
        this.software = server.getName();
        this.imageURL = settings.getIcon();
        this.host = server.getIp();
        this.status = settings.getStatus().getId();
    }

    @Override
    protected PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("""
                REPLACE INTO servers (
                          identifier,
                          address,
                          port,
                          game,
                          playeramount,
                          capacity,
                          software,
                          lastupdate,
                          image,
                          status
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, UNIX_TIMESTAMP(), ?, ?)""");

        statement.setString(1, identifier);
        statement.setString(2, host);
        statement.setInt(3, port);
        statement.setString(4, gameMode);
        statement.setInt(5, playerAmount);
        statement.setInt(6, capacity);
        statement.setString(7, software);
        statement.setString(8, imageURL);
        statement.setInt(9, status);

        return statement;
    }
}
