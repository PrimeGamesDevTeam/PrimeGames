/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task;

import net.primegames.providor.MySqlTask;
import net.primegames.utils.CoreLogger;

import java.sql.SQLException;
import java.sql.Statement;

final public class MySQLInitialCoreTask extends MySqlTask {

    public void doOperations(Statement statement) throws SQLException {
        CoreLogger.info("Initializing Core database tables");
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users(" +
                        "id BIGINT(20) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "uuid BINARY(16) UNIQUE KEY," +
                        "username VARCHAR(17) UNIQUE," +
                        "last_ip VARCHAR(16) DEFAULT '0.0.0.0'," +

                        "reputation INT DEFAULT 0," +

                        "common_keys INT DEFAULT 0," +
                        "vote_keys INT DEFAULT 0," +
                        "rare_keys INT DEFAULT 0," +
                        "legendary_keys INT DEFAULT 0," +

                        "locale VARCHAR(6) DEFAULT 'eng'," +
                        "continent_code VARCHAR(2) DEFAULT '??'," +
                        "country_code VARCHAR(2) DEFAULT '??'," +

                        "time_played INT DEFAULT 0," +
                        "last_connection_duration INT DEFAULT 0," +

                        "warnings TINYINT(1) DEFAULT 0," +

                        "last_connection DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "registered_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                        ") ENGINE=INNODB");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_ips( " +
                "id BIGINT(20) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                "ip VARCHAR(16) DEFAULT '0.0.0.0', " +
                "username VARCHAR(16), " +
                "is_banned BOOLEAN DEFAULT false" +
                ") ENGINE=INNODB");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_groups( " +
                "user_id BIGINT(20) UNSIGNED NOT NULL, " +
                "group_id TINYINT UNSIGNED NOT NULL, " +
                "assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP , " +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ") ENGINE=INNODB");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS sentences( " +
                "id BIGINT(20) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                "username VARCHAR(17), " +
                "uuid BINARY(16), " +
                "ip VARCHAR(16), " +
                "issuer VARCHAR(16), " +
                "reason VARCHAR(200), " +
                "category TINYINT UNSIGNED, " +

                "expires_at DATETIME, " +
                "issued_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=INNODB");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS command_queue( " +
                "target VARCHAR(17), " +
                "command VARCHAR(200)" +
                ") ENGINE=INNODB");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS servers( " +
                "identifier VARCHAR(7) PRIMARY KEY, " +
                "address VARCHAR(20), " +
                "port SMALLINT UNSIGNED, " +
                "game VARCHAR(20), " +
                "playeramount TINYINT, " +
                "capacity TINYINT, " +
                "software VARCHAR(20), " +
                "image TEXT, " +
                "lastupdate BIGINT" +
                ") ENGINE=INNODB");
    }
}
