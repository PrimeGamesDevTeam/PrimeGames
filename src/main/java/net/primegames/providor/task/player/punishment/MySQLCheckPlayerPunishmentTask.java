/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player.punishment;

import net.primegames.PrimeGames;
import net.primegames.player.BedrockPlayer;
import net.primegames.player.BedrockPlayerManager;
import net.primegames.providor.task.MySqlFetchQueryTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.UUID;


final public class MySQLCheckPlayerPunishmentTask extends MySqlFetchQueryTask {

    private final UUID bedrockUUid;
    private final UUID serverUuid;

    public MySQLCheckPlayerPunishmentTask(UUID bedrockUuid, UUID serverUuid) throws SQLException {
        super(PrimeGames.plugin(), PrimeGames.defaultConnection());
        verifyPlayer(serverUuid, bedrockUuid);
        this.serverUuid = serverUuid;
        this.bedrockUUid = bedrockUuid;
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT expires_at, issuer, reason, category FROM sentences WHERE (uuid = UUID_TO_BIN(?) AND expires_at IS NULL) OR (uuid = UUID_TO_BIN(?) AND expires_at > ?)");
        statement.setString(1, bedrockUUid.toString());
        statement.setString(2, bedrockUUid.toString());
        statement.setDate(3, new Date(System.currentTimeMillis()));
        return statement;
    }

    @Override
    protected void handleResult(ResultSet resultSet) throws SQLException {
        Player player = Bukkit.getPlayer(serverUuid);
        if(player != null){
            BedrockPlayer bedrockPlayer = BedrockPlayerManager.getInstance().getPlayer(player);
            while (resultSet.next()){
                Date expiresAt = resultSet.getDate("expires_at");
                if(expiresAt == null || (new Timestamp(new Date(System.currentTimeMillis()).getTime()).getTime() < new Timestamp(expiresAt.getTime()).getTime())){
                    applySentence(player, bedrockPlayer, resultSet.getInt("category"), resultSet.getString("issuer"), resultSet.getString("reason"), expiresAt);
                }
            }
        } else {
            LoggerUtils.info("Player " + serverUuid + " is not online, skipping punishment check");
        }
    }

    private void applySentence(Player player, BedrockPlayer bedrockPlayer, int category, String effector, String reason, Date expiresAt){
            switch (category) {
                case PunishmentCategory.BAN -> {
                    if (expiresAt == null) {
                        player.kickPlayer(ChatColor.RED + "You have been banned from this server.\n\n");
                        return;
                    }
                    long timeLeft = new Timestamp(expiresAt.getTime()).getTime() - new Timestamp(new Date(System.currentTimeMillis()).getTime()).getTime();
                    if (timeLeft > 0) {
                        long days = timeLeft / (1000 * 60 * 60 * 24);
                        long hours = (timeLeft - (days * 1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                        long minutes = (timeLeft - (days * 1000 * 60 * 60 * 24) - (hours * 1000 * 60 * 60)) / (1000 * 60);
                        player.kickPlayer(ChatColor.RED + "You have been banned by " + effector + " for " + reason + " (" + days + " days, " + hours + " hours, " + minutes + " minutes)");
                    }
                }
                case PunishmentCategory.MUTE -> bedrockPlayer.mute(expiresAt);
                default -> LoggerUtils.error("Unhandled category on the sentence for " + player.getName() + "Category: " + category);
            }
    }
}
