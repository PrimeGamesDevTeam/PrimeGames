package net.primegames.providor.task.gameserver;

import net.primegames.providor.MySqlTask;

import java.sql.SQLException;
import java.sql.Statement;

public class MySQLCleanDeadServerData extends MySqlTask {


    @Override
    protected void doOperations(Statement statement) throws SQLException {
        statement.executeUpdate("DELETE FROM servers WHERE lastupdate < UNIX_TIMESTAMP() - 180");
    }

}
