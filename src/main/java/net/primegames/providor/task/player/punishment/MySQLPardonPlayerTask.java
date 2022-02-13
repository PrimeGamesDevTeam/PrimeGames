/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player.punishment;

import net.primegames.providor.MySqlPostQueryTask;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

final public class MySQLPardonPlayerTask extends MySqlPostQueryTask {

    public MySQLPardonPlayerTask(UUID bedrockUUID) {

    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("//todo");
    }
}
