package net.primegames.core.providor;

import net.primegames.core.PrimesCore;
import net.primegames.core.utils.CoreLogger;
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
        return PrimesCore.getInstance().getMySQLProvider().getConnection();
    }

    protected abstract void doOperations(Statement statement) throws SQLException;

}
