/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.core.providor;


import net.primegames.core.PrimesCore;
import net.primegames.core.utils.CoreLogger;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionBuilder {

    private Connection connection;

    public MySqlConnectionBuilder() {
        open(PrimesCore.getInstance().getConfig());
    }

    protected void open(FileConfiguration config) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUri = "jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getString("mysql.port", "3306") + "/" + config.getString("mysql.database") + "?autoReconnect=true&useGmtMillisForDatetimes=true&serverTimezone=GMT";
            connection = DriverManager.getConnection(connectionUri, config.getString("mysql.username"), config.getString("mysql.password"));
            connection.setAutoCommit(true);
            CoreLogger.info("Mysql Connection to Core Database successfully established");
        } catch (SQLException ex) {
            CoreLogger.warn("Could not Establish connection with MySQL database");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            CoreLogger.error("MySQL Driver is missing.. Needs dblib");
            ex.printStackTrace();
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
