/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor;


import net.primegames.JavaCore;
import net.primegames.utils.LoggerUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionBuilder {

    private Connection connection;

    private static int tries = 0;

    public static MySqlConnectionBuilder build(JavaCore core) {
        JavaPlugin plugin = core.getPlugin();
        File configFile = new File(plugin.getDataFolder(), "mysql.yml");
        MySqlConnectionBuilder builder;
        if (configFile.exists()) {
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            return new MySqlConnectionBuilder(getCredentials(config));
        } else {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("mysql.yml", false);
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(configFile);
                setDefaults(config);
                config.save(configFile);
            } catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }
            if (tries < 10) {
                build(core);
                tries++;
            } else {
                throw new RuntimeException("Could not create mysql.yml");
            }
        }
       throw new RuntimeException("Could not create mysql.yml");
    }

    private MySqlConnectionBuilder(MysqlCredentials credentials) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUri = "jdbc:mysql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDatabase() + "?autoReconnect=true&useGmtMillisForDatetimes=true&serverTimezone=GMT";
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

    private static MysqlCredentials getCredentials(YamlConfiguration config) {
        String host = config.getString("host", "127.0.0.1");
        int port = config.getInt("port", 3306);
        String database = config.getString("database", "core");
        String username = config.getString("username", "root");
        String password = config.getString("password", "password");
        return new MysqlCredentials(host, username, password, database, port);
    }

    private static void setDefaults(YamlConfiguration config) {
        config.set("host", "172.0.0.1");
        config.set("port", 3306);
        config.set("database", "database");
        config.set("username", "username");
        config.set("password", "password");
    }

}
