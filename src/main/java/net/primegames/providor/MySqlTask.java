package net.primegames.providor;

import net.primegames.JavaCore;
import net.primegames.utils.CoreLogger;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class MySqlTask extends BukkitRunnable {

    public void run() {
        Statement statement;
        try {
            statement = getConnection().createStatement();
            doOperations(statement);
        } catch (SQLException exception) {
            CoreLogger.error("Failed to initialize database tables");
            exception.getStackTrace();
        }
    }

    public Connection getConnection() {
        return JavaCore.getInstance().getMySQLprovider().getConnection();
    }

    protected abstract void doOperations(Statement statement) throws SQLException;

}
