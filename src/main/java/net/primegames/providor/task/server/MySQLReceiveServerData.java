package net.primegames.providor.task.server;

import net.primegames.PrimeGames;
import net.primegames.providor.connection.ConnectionId;
import net.primegames.providor.task.MySqlFetchQueryTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLReceiveServerData extends MySqlFetchQueryTask {

    public MySQLReceiveServerData() throws SQLException {
        super(PrimeGames.plugin(), PrimeGames.getInstance().getMySQLprovider().getConnection(ConnectionId.CORE));
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM `servers`");
    }

    @Override
    protected void handleResult(ResultSet resultSet) throws SQLException {
        this.verifyPluginEnabled(PrimeGames.getInstance().getPlugin());
        while (resultSet.next()) {
            PrimeGames.getInstance().getGameServerManager().addServer(resultSet.getString("identifier"), resultSet.getString("game"), resultSet.getString("address"), resultSet.getInt("port"), resultSet.getInt("playeramount"), resultSet.getInt("capacity"), resultSet.getString("software"), resultSet.getString("image"), resultSet.getInt("status"));
        }
    }

}
