package net.primegames.providor.task;

import net.primegames.providor.MySqlProvider;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public abstract class MySqlTask extends ProviderRunnable {

    private final Connection connection;

    public MySqlTask(Connection connection) {
        this.connection = connection;
    }

    public void run() {
        Statement statement;
        try {
            statement = connection.createStatement();
            doOperations(statement);
        } catch (SQLException exception) {
            Bukkit.getLogger().log(Level.SEVERE, MySqlProvider.PREFIX + "Failed to initialize database tables");
            exception.getStackTrace();
        }
    }

    protected abstract void doOperations(Statement statement) throws SQLException;

}
