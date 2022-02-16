/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player;

import net.primegames.PrimeGames;
import net.primegames.event.player.BedrockPlayerLoadedEvent;
import net.primegames.player.BedrockPlayer;
import net.primegames.player.BedrockPlayerManager;
import net.primegames.providor.connection.ConnectionId;
import net.primegames.providor.task.MySqlFetchQueryTask;
import net.primegames.providor.task.player.punishment.MySQLCheckPlayerPunishmentTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

final public class PlayerLoadTask extends MySqlFetchQueryTask {

    private final UUID bedrockUuid;
    private final UUID serverUuid;
    private final String name;

    public PlayerLoadTask(UUID uuid, Player player, String username) throws SQLException {
        super(PrimeGames.plugin(), PrimeGames.getInstance().getMySQLprovider().getConnection(ConnectionId.CORE));
        LoggerUtils.debug("Initiating data load task for " + player.getName() + " with uuid: " + uuid + "from MySQL");
        this.bedrockUuid = uuid;
        this.serverUuid = player.getUniqueId();
        this.name = (username != null) ? username : player.getName();
    }

    @Override
    protected @NotNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        LoggerUtils.debug("preparing stmt load task for " + name);
        PreparedStatement statement = connection.prepareStatement("SELECT users.*, GROUP_CONCAT(user_groups.group_id) AS all_groups FROM users " +
                "LEFT JOIN user_groups ON user_groups.user_id = users.id" +
                " WHERE uuid = UUID_TO_BIN(?) " +
                "GROUP BY users.id " +
                "LIMIT 1");
        statement.setString(1, bedrockUuid.toString());
        return statement;
    }

    @Override
    protected void handleResult(ResultSet resultSet) throws SQLException {
        Player player = getServer().getPlayer(serverUuid);
        if (player != null) {
            //todo do rank setups etc here as per the plugin
            if (resultSet.next()) {
                try {
                    BedrockPlayer bedrockPlayer = new BedrockPlayer(
                            resultSet.getInt("id"),
                            bedrockUuid,
                            serverUuid,
                            this.name,
                            resultSet.getString("last_ip"),
                            (player.getAddress() != null) ? player.getAddress().getHostName() : resultSet.getString("last_ip"),
                            resultSet.getString("country_code"),
                            resultSet.getString("continent_code"),
                            resultSet.getInt("reputation"),
                            resultSet.getInt("warnings"),
                            resultSet.getLong("time_played"),
                            resultSet.getLong("last_connection_duration"),
                            resultSet.getDate("registered_at"),
                            resultSet.getInt("vote_keys"),
                            resultSet.getInt("common_keys"),
                            resultSet.getInt("rare_keys"),
                            resultSet.getInt("legendary_keys"),
                            player.locale().toString()
                    );
                    BedrockPlayerManager.getInstance().addPlayer(bedrockPlayer);
                    String[] groupIds = resultSet.getString("all_groups").split(",");
                    ArrayList<Integer> groupIdList = new ArrayList<>();
                    for (String groupdId : groupIds) {
                        groupIdList.add(Integer.parseInt(groupdId));
                    }
                    bedrockPlayer.addGroups(groupIdList);
                    (new BedrockPlayerLoadedEvent(player, bedrockPlayer)).callEvent();
                    PrimeGames.getInstance().getMySQLprovider().scheduleTask(new MySQLCheckPlayerPunishmentTask(bedrockUuid, serverUuid));
                    LoggerUtils.info("Bedrock Player: " + name + " successfully loaded for with Core UUID: " + bedrockUuid);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } else {
                LoggerUtils.info("Data was not found for " + name + " Initiating new registration");
                PrimeGames.getInstance().getMySQLprovider().scheduleTask(new PlayerRegisterTask(bedrockUuid, player));
            }
        } else {
            LoggerUtils.warn("Player disconnected while data was being loaded");
        }
        resultSet.close();
    }
}
