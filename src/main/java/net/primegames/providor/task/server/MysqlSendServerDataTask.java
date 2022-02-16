package net.primegames.providor.task.server;

import net.primegames.PrimeGames;
import net.primegames.providor.connection.ConnectionId;
import net.primegames.providor.task.MySqlPostQueryTask;
import net.primegames.server.GameServerSettings;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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


    public MysqlSendServerDataTask(GameServerSettings settings) throws SQLException {
        super(PrimeGames.getInstance().getMySQLprovider().getConnection(ConnectionId.CORE), PrimeGames.plugin());
        Server server = PrimeGames.getInstance().getPlugin().getServer();
        this.identifier = settings.getIdentifier();
        this.gameMode = settings.getGameMode().name();
        this.port = settings.getPort();
        this.playerAmount = server.getOnlinePlayers().size();
        this.capacity = server.getMaxPlayers();
        this.software = server.getName();
        this.imageURL = settings.getIcon();
        this.host = settings.getAddress();
        this.status = settings.getStatus().getId();
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
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
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, UNIX_TIMESTAMP(), ?, ?)""", Statement.RETURN_GENERATED_KEYS);

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
