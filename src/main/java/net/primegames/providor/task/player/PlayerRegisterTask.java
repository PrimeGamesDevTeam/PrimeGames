/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player;

import net.primegames.event.player.CorePlayerRegisteredEvent;
import net.primegames.player.BedrockPlayer;
import net.primegames.player.BedrockPlayerManager;
import net.primegames.providor.MySqlPostQueryTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

final public class PlayerRegisterTask extends MySqlPostQueryTask {

    private final UUID bedrockUuid;
    private final UUID serverUuid;
    private final String userName;
    private String address = "0.0.0.0";

    public PlayerRegisterTask(UUID bedrockUuid, Player player) {
        this.bedrockUuid = bedrockUuid;
        this.serverUuid = player.getUniqueId();
        userName = player.getName();
        if (player.getAddress() != null) {
            address = player.getAddress().getHostName();
        }
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (uuid, username, last_ip) VALUES (UUID_TO_BIN(?), ?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, bedrockUuid.toString());
        statement.setString(2, userName);
        statement.setString(3, address);
        return statement;
    }

    @Override
    protected void onInsert(int id) {
        Player player = getServer().getPlayer(serverUuid);
        if (player != null) {
            String address = (player.getAddress() != null) ? player.getAddress().getHostName() : "0.0.0.0";
            BedrockPlayerManager.getInstance().addPlayer(new BedrockPlayer(
                    id,
                    bedrockUuid,
                    serverUuid,
                    player.getName(),
                    address,
                    address,
                    "??",
                    "??",
                    0,
                    0,
                    0,
                    0,
                    new Date(),
                    0,
                    0,
                    0,
                    0,
                    "??"
            ));
            (new CorePlayerRegisteredEvent(player)).callEvent();
            LoggerUtils.debug("new registration successful for " + player.getName());
        } else {
            LoggerUtils.debug(userName + " logged out before their registration could be completed");
        }
    }
}
