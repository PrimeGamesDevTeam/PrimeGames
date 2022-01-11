package net.primegames.providor;

import net.primegames.PrimeGames;
import net.primegames.utils.LoggerUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class MySqlTask extends ProviderRunnable {

    public void run() {
        Statement statement;
        try {
            statement = getConnection().createStatement();
            doOperations(statement);
        } catch (SQLException exception) {
            LoggerUtils.error("Failed to initialize database tables");
            exception.getStackTrace();
        }
    }

    public Connection getConnection() {
        return PrimeGames.getInstance().getMySQLprovider().getConnection();
    }

    protected abstract void doOperations(Statement statement) throws SQLException;

}
