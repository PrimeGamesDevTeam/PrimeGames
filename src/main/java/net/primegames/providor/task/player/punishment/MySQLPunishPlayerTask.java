/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player.punishment;

import net.primegames.player.BedrockPlayer;
import net.primegames.player.BedrockPlayerManager;
import net.primegames.providor.MySqlPostQueryTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

final public class MySQLPunishPlayerTask extends MySqlPostQueryTask {


    private final String username;
    private final String effector;
    private final String reason;
    private final UUID bedrockUuid;
    private final UUID serverUuid;
    private final Date expiration;
    private final String ip;
    private final int category;

    public MySQLPunishPlayerTask(String username, String effector, String reason, UUID bedrockUuid, UUID serverUuid, Date expiration, String lastAddress, int category){
        this.username = username;
        this.effector = effector;
        this.reason = reason;
        ip = lastAddress;
        this.serverUuid = serverUuid;
        this.bedrockUuid = bedrockUuid;
        this.expiration = expiration;
        this.category = category;
    }
    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO sentences(username,uuid,ip,issuer,reason,expires_at,category ) VALUES (?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, username);
        statement.setString(2, bedrockUuid.toString());
        statement.setString(3, ip);
        statement.setString(4, effector);
        statement.setString(5, reason);
        statement.setDate(6, expiration);
        statement.setInt(7, category);
        return statement;
    }

    @Override
    protected void onInsert(int Id) {
        Player player = Bukkit.getPlayer(serverUuid);
        if(player != null){
            BedrockPlayer bedrockPlayer = BedrockPlayerManager.getInstance().getPlayer(player);
            switch (category) {
                case PunishmentCategory.BAN -> {
                    long banExpirationTime = expiration.getTime();
                    long daysLeft = (banExpirationTime - System.currentTimeMillis()) / 86400000;
                    long hoursLeft = (banExpirationTime - System.currentTimeMillis()) / 3600000;
                    long minutesLeft = (banExpirationTime - System.currentTimeMillis()) / 60000;
                    player.kickPlayer(ChatColor.RED + "You have been banned from this server for " + reason + "for " + daysLeft + " days, " + hoursLeft + " hours and " + minutesLeft + " minutes.");
                }
                case PunishmentCategory.MUTE -> bedrockPlayer.mute(expiration);
                default -> LoggerUtils.error("Unhandled category on the sentence for " + username + "Category: " + category);
            }
        } else {
            LoggerUtils.info("Player is not online, but the punishment was sent to the database");
        }
    }
}
