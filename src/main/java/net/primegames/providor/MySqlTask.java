package net.primegames.providor;

import net.primegames.PrimesCore;
import net.primegames.utils.LoggerUtils;
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
            LoggerUtils.error("Failed to initialize database tables");
            exception.getStackTrace();
        }
    }

    public Connection getConnection(){
        return PrimesCore.getInstance().getMySQLProvider().getConnection();
    }

    protected abstract void doOperations(Statement statement) throws SQLException;

}
