package net.primegames.providor.task.server;

import net.primegames.PrimeGames;
import net.primegames.providor.task.MySqlPostQueryTask;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlDeleteServerData extends MySqlPostQueryTask {

    private final String serverId;

    public MysqlDeleteServerData(String serverId) throws SQLException {
        super(PrimeGames.defaultConnection(), PrimeGames.plugin());
        this.serverId = serverId;
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM `servers` WHERE `identifier` = ?");
        statement.setString(1, serverId);
        return statement;
    }

}
