/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor;

import lombok.Getter;
import net.primegames.providor.connection.ConnectionId;
import net.primegames.providor.connection.MySqlConnectionBuilder;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySqlProvider {

    public static final String PREFIX = ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "MySql" + ChatColor.DARK_PURPLE + "] " + ChatColor.RESET;

    @Getter
    private final Plugin plugin;
    private final Map<ConnectionId, Connection> connections = new HashMap<>();

    public MySqlProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleTask(BukkitRunnable mySqlTask) {
        mySqlTask.runTaskAsynchronously(this.plugin);
    }


    public Map<ConnectionId, Connection> getConnections() {
        return connections;
    }

    public Connection getConnection(ConnectionId connectionId) throws SQLException {
        Connection connection = this.connections.get(connectionId);
        if (connection.isClosed()){
            throw new SQLException("Tried to get a closed SQL Connection with ID: " + connectionId.toString());
        }
        return this.connections.get(connectionId);
    }

    public void createConnection(ConnectionId connectionId, MysqlCredentials credentials){
        try {
            this.connections.put(connectionId, credentials.createConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public MySqlConnectionBuilder buildCoreConnection(Plugin plugin) {
        plugin.saveDefaultConfig();
        return new MySqlConnectionBuilder(MysqlCredentials.getCredentials(plugin, "core"));
    }


    public void remove(ConnectionId connectionId) {
        this.connections.remove(connectionId);
    }
}
