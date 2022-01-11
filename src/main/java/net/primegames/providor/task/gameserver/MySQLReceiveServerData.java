package net.primegames.providor.task.gameserver;

import net.primegames.PrimeGames;
import net.primegames.providor.MySqlFetchQueryTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLReceiveServerData extends MySqlFetchQueryTask {

    @Override
    protected PreparedStatement prepareStatement(Connection connection) throws SQLException {
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
