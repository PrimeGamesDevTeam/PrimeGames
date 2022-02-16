package net.primegames.providor.task.server;

import net.primegames.PrimeGames;
import net.primegames.providor.connection.ConnectionId;
import net.primegames.providor.task.MySqlTask;

import java.sql.SQLException;
import java.sql.Statement;

public class MySQLCleanDeadServerData extends MySqlTask {


    public MySQLCleanDeadServerData() throws SQLException {
        super(PrimeGames.getInstance().getMySQLprovider().getConnection(ConnectionId.CORE));
    }

    @Override
    protected void doOperations(Statement statement) throws SQLException {
        statement.executeUpdate("DELETE FROM servers WHERE lastupdate < UNIX_TIMESTAMP() - 180");
    }

}
