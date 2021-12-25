/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.core.providor.task.player.punishment;

import net.primegames.core.providor.MySqlPostQueryTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

final public class MySQLPunishPlayerTask extends MySqlPostQueryTask {


    @Override
    protected PreparedStatement preparedStatement(Connection connection) throws SQLException {
        return null;
    }

//    private String username;
//    private String effector;
//    private String reason;
//    private String uniqueId;
//    private Date expiration;
//    private String ip;
//    private int category;
//
//    public MySQLPunishPlayerTask(String username, String effector, String reason, String uniqueId, Date expiration, String lastAddress, int category){
//        this.username = username;
//        this.effector = effector;
//        this.reason = reason;
//        this.reason = reason;
//        ip = lastAddress;
//        this.uniqueId = uniqueId;
//        this.expiration = expiration;
//        this.category = category;
//    }
//    @Override
//    protected PreparedStatement preparedStatement(Connection connection) throws SQLException {
//        PreparedStatement statement = connection.prepareStatement("INSERT INTO sentences(username,uuid,ip,issuer,reason,expires_at,category ) VALUES (?, ?, ?, ?, ?, ?, ?)");
//        statement.setString(1, username);
//        statement.setString(2, uniqueId);
//        statement.setString(3, ip);
//        statement.setString(4, effector);
//        statement.setString(5, reason);
//        statement.setDate(6, expiration);
//        statement.setInt(7, category);
//        return statement;
//    }
//
//    @Override
//    protected void onInsert(int Id) {
//        Player player = Server.getInstance().getPlayer(username);
//        if(player != null){
//            Player corePlayer = Player.cast(player);
//            switch (category){
//                case PunishmentCategory.BAN:
//                    player.kick(TextFormat.RED.toString() + "You have been banned on this server\nReason: " + reason + "\nBanned By: " + effector); //todo provide info if the player is perm bannned or temp and with date
//                    break;
//                case PunishmentCategory.MUTE:
//                    corePlayer.mute();
//                    break;
//                default:
//                    LoggerUtils.error("Unhandled category on the sentence for "+ username +  "Category: " + category);
//            }
//        }
//    }
}
