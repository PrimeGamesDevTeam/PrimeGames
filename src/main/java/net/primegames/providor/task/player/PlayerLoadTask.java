/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player;

import net.primegames.JavaCore;
import net.primegames.event.player.CorePlayerLoadedEvent;
import net.primegames.player.CorePlayer;
import net.primegames.player.CorePlayerManager;
import net.primegames.providor.MySqlFetchQueryTask;
import net.primegames.utils.CoreLogger;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

final public class PlayerLoadTask extends MySqlFetchQueryTask {

    private final UUID originalUuid;
    private final UUID serverUuid;
    private final String name;

    public PlayerLoadTask(UUID uuid, Player player) {
        CoreLogger.debug("Initiating data load task for " + player.getName() + " with uuid: " + uuid + "from MySQL");
        this.originalUuid = uuid;
        this.serverUuid = player.getUniqueId();
        this.name = player.getName();
    }

    @Override
    protected PreparedStatement prepareStatement(Connection connection) throws SQLException {
        CoreLogger.debug("preparing stmt load task for " + name);
        PreparedStatement statement = connection.prepareStatement("SELECT users.*, GROUP_CONCAT(user_groups.group_id) AS all_groups FROM users " +
                "LEFT JOIN user_groups ON user_groups.user_id = users.id" +
                " WHERE uuid = UUID_TO_BIN(?) " +
                "GROUP BY users.id " +
                "LIMIT 1");
        statement.setString(1, originalUuid.toString());
        return statement;
    }

    @Override
    protected void handleResult(ResultSet resultSet) throws SQLException {
        Player player = JavaCore.getInstance().getServer().getPlayer(serverUuid);
        if (player != null) {
            //todo do rank setups etc here as per the plugin
            if (resultSet.next()) {
                try {
                    CorePlayer corePlayer = new CorePlayer(
                            resultSet.getInt("id"),
                            originalUuid,
                            serverUuid,
                            player.getName(),
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
                    CorePlayerManager.getInstance().addPlayer(corePlayer);
                    String[] groupIds = resultSet.getString("all_groups").split(",");
                    ArrayList<Integer> groupIdList = new ArrayList<>();
                    for (String groupdId : groupIds) {
                        groupIdList.add(Integer.parseInt(groupdId));
                    }
                    corePlayer.addGroups(groupIdList);
                    (new CorePlayerLoadedEvent(player, corePlayer)).callEvent();
                    CoreLogger.info("Core Player: " + player.getName() + " successfully loaded for with Core UUID: " + originalUuid);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } else {
                CoreLogger.info("Data was not found for " + player.getName() + " Initiating new registration");
                JavaCore.getInstance().getMySQLProvider().scheduleTask(new PlayerRegisterTask(originalUuid, player));
            }
        } else {
            CoreLogger.warn("Player disconnected while data was being loaded");
        }
        resultSet.close();
    }
}
