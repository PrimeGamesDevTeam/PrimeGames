/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player.punishment;

import net.primegames.providor.MySqlPostQueryTask;

import java.sql.Connection;
import java.sql.PreparedStatement;

final public class MySQLPardonPlayerTask extends MySqlPostQueryTask {
    @Override
    protected PreparedStatement preparedStatement(Connection connection)  {
        return null;
    }
}
