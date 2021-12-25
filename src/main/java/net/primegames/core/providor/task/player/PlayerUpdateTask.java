/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.core.providor.task.player;

import net.primegames.core.player.CorePlayerData;
import net.primegames.core.player.PlayerStatus;
import net.primegames.core.providor.MySqlPostQueryTask;
import net.primegames.core.utils.LoggerUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

final public class PlayerUpdateTask extends MySqlPostQueryTask {

    private final CorePlayerData playerData;

    public PlayerUpdateTask(CorePlayerData db){
        playerData = db;
    }

    @Override
    protected PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET" +
                "uuid = UUID_TO_BIN(?)," +
                "username = ?," +
                "last_ip = ?," +

                "reputation = ?," +

                "locale = ?," +
                "continent_code = ?," +
                "country_code = ?," +

                "common_keys = ?," +
                "vote_keys = ?," +
                "rare_keys = ?," +
                "legendary_keys = ?," +

                "time_played = ?," +
                "last_connection_duration = ?," +

                "warnings = ?," +

                "last_connection = CURRENT_TIMESTAMP" +
                "WHERE id = ?");
        statement.setString(1, playerData.getUuid().toString());
        statement.setString(2, playerData.getUsername());
        statement.setString(3, playerData.getCurrentIp());
        statement.setInt(4, playerData.getReputation());
        statement.setString(5, playerData.getLocale());
        statement.setString(6, playerData.getContinentCode());
        statement.setString(7, playerData.getCountryCode());
        statement.setInt(8, playerData.getCommonKeys());
        statement.setInt(9, playerData.getVoteKeys());
        statement.setInt(10, playerData.getRareKeys());
        statement.setInt(11, playerData.getLegendaryKeys());
        statement.setLong(12, playerData.getTimePlayed());
        statement.setLong(13, playerData.getLastSessionDuration());

        statement.setInt(14, playerData.getWarnings());
        statement.setInt(15, playerData.getInternalId());

        return statement;
    }

    @Override
    protected void onInsert(int Id) {
        if (playerData.isOnline()){
             playerData.setPlayerStatus(PlayerStatus.ONLINE);
        }else {
            LoggerUtils.info("Player " + playerData.getUsername() + " logged out before their data could be saved");
        }
    }
}
