/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player;

import net.primegames.player.BedrockPlayer;
import net.primegames.providor.MySqlPostQueryTask;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

final public class PlayerUpdateTask extends MySqlPostQueryTask {

    private final BedrockPlayer corePlayer;

    public PlayerUpdateTask(BedrockPlayer player) {
        this.corePlayer = player;
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
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
        statement.setString(1, corePlayer.getBedrockUUID().toString());
        statement.setString(2, corePlayer.getUsername());
        statement.setString(3, corePlayer.getCurrentIp());
        statement.setInt(4, corePlayer.getReputation());
        statement.setString(5, corePlayer.getLocale());
        statement.setString(6, corePlayer.getContinentCode());
        statement.setString(7, corePlayer.getCountryCode());
        statement.setInt(8, corePlayer.getCommonKeys());
        statement.setInt(9, corePlayer.getVoteKeys());
        statement.setInt(10, corePlayer.getRareKeys());
        statement.setInt(11, corePlayer.getLegendaryKeys());
        statement.setLong(12, corePlayer.getTimePlayed());
        statement.setLong(13, corePlayer.getLastSessionDuration());

        statement.setInt(14, corePlayer.getWarnings());
        statement.setInt(15, corePlayer.getInternalId());

        return statement;
    }

    @Override
    protected void onInsert(int Id) {
        //todo
    }
}
