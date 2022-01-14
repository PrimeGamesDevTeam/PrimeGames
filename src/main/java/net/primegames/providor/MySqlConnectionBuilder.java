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
        return new MySqlConnectionBuilder(getCredentials(plugin));
    }

    private MySqlConnectionBuilder(MysqlCredentials credentials) {
        LoggerUtils.info("Connecting to MySQL...");
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

    private static MysqlCredentials getCredentials(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        if (config.getString("core.mysql.host") == null || config.getString("core.mysql.port") == null || config.getString("core.mysql.database") == null || config.getString("core.mysql.username") == null || config.getString("core.mysql.password") == null) {
            LoggerUtils.warn("MySQL Credentials are missing in config.yml" + " of " + plugin.getName() + " plugin. Setting defaults...");
            setDefaults(config);
            plugin.saveConfig();
            LoggerUtils.warn("MySQL Credentials are set to defaults, Make sure to set them correctly in config.yml of " + plugin.getName() + " plugin.");
        }
        String host = config.getString("core.mysql.host");
        int port = config.getInt("core.mysql.port");
        String database = config.getString("core.mysql.database");
        String username = config.getString("core.mysql.username");
        String password = config.getString("core.mysql.password");
        return new MysqlCredentials(host, username, password, database, port);
    }

    private static void setDefaults(FileConfiguration config) {
        config.set("core.mysql.host", "127.0.0.1");
        config.set("core.mysql.port", 3306);
        config.set("core.mysql.database", "core");
        config.set("core.mysql.username", "root");
        config.set("core.mysql.password", "password");
    }

}
