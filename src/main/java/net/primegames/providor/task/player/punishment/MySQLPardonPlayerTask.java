/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player.punishment;

import net.primegames.providor.task.MySqlPostQueryTask;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

final public class MySQLPardonPlayerTask extends MySqlPostQueryTask {


    public MySQLPardonPlayerTask(Connection connection, Plugin plugin) {
        super(connection, plugin);
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("//todo");
    }
}
