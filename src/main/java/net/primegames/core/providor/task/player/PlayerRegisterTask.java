/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.core.providor.task.player;

import net.primegames.core.PrimesCore;
import net.primegames.core.event.player.CorePlayerRegisteredEvent;
import net.primegames.core.player.CorePlayer;
import net.primegames.core.player.CorePlayerManager;
import net.primegames.core.providor.MySqlPostQueryTask;
import net.primegames.core.utils.LoggerUtils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

final public class PlayerRegisterTask extends MySqlPostQueryTask {

    private final UUID originalUuid;
    private final UUID serverUuid;
    private final String userName;
    private String address = "0.0.0.0";

    public PlayerRegisterTask(UUID uuid, Player player){
        this.originalUuid = uuid;
        this.serverUuid = player.getUniqueId();
        userName = player.getName();
        if (player.getAddress() != null) {
            address = player.getAddress().getHostName();
        }
    }

    @Override
    protected PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (uuid, username, last_ip) VALUES (UUID_TO_BIN(?), ?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, originalUuid.toString());
        statement.setString(2, userName);
        statement.setString(3, address);
        return statement;
    }

    @Override
    protected void onInsert(int id) {
        Player player = PrimesCore.getInstance().getServer().getPlayer(serverUuid);
        if(player != null) {
            String address = (player.getAddress() != null) ? player.getAddress().getHostName() : "0.0.0.0";
            CorePlayerManager.getInstance().addPlayer(new CorePlayer(
                    id,
                    originalUuid,
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
            LoggerUtils.debug( userName + " logged out before their registration could be completed");
        }
    }
}
