/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor;


import net.primegames.PrimeGames;
import net.primegames.utils.LoggerUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionBuilder {

    private Connection connection;

    public static MySqlConnectionBuilder build(PrimeGames core) {
        JavaPlugin plugin = core.getPlugin();
        plugin.saveDefaultConfig();
        return new MySqlConnectionBuilder(getCredentials(plugin.getConfig(), plugin));
    }

    private MySqlConnectionBuilder(MysqlCredentials credentials) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUri = "jdbc:mysql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDatabase() + "?autoReconnect=true&useGmtMillisForDatetimes=true&serverTimezone=GMT";
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(connectionUri, credentials.getUsername(), credentials.getPassword());
            connection.setAutoCommit(true);
            LoggerUtils.info("Mysql Connection to Core Database successfully established");
        } catch (SQLException ex) {
            LoggerUtils.warn("Could not Establish connection with MySQL database");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            LoggerUtils.error("MySQL Driver is missing.. Needs dblib");
            ex.printStackTrace();
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

    private static MysqlCredentials getCredentials(FileConfiguration config, JavaPlugin plugin) {
        if (config.getString("mysql.host") == null || config.getString("mysql.port") == null || config.getString("mysql.database") == null || config.getString("mysql.username") == null || config.getString("mysql.password") == null) {
            setDefaults(config);
            plugin.saveDefaultConfig();
        }
        String host = config.getString("mysql.host");
        int port = config.getInt("mysql.port");
        String database = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");
        return new MysqlCredentials(host, username, password, database, port);
    }

    private static void setDefaults(FileConfiguration config) {
        config.set("mysql.host", "172.0.0.1");
        config.set("mysql.port", 3306);
        config.set("mysql.database", "core");
        config.set("mysql.username", "root");
        config.set("mysql.password", "password");
    }

}
