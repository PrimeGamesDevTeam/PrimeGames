/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task.player;

import net.primegames.PrimesCore;
import net.primegames.event.player.CorePlayerLoadedEvent;
import net.primegames.player.CorePlayer;
import net.primegames.player.CorePlayerData;
import net.primegames.providor.MySqlFetchQueryTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

final public class PlayerLoadTask extends MySqlFetchQueryTask {

    private final UUID uuid;

    private final String name;

    public PlayerLoadTask(UUID uuid, Player player){
        LoggerUtils.debug("Initiating data load task for " + player.getName() + " with uuid: " + uuid + "from MySQL");
        this.uuid = uuid;
        this.name = player.getName();
    }

    @Override
    protected PreparedStatement prepareStatement(Connection connection) throws SQLException {
        LoggerUtils.debug("preparing stmt load task for " + name);
        PreparedStatement statement = connection.prepareStatement("SELECT users.*, GROUP_CONCAT(user_groups.group_id) AS all_groups FROM users " +
                        "LEFT JOIN user_groups ON user_groups.user_id = users.id" +
                        " WHERE uuid = UUID_TO_BIN(?) " +
                        "GROUP BY users.id " +
                        "LIMIT 1");
        statement.setString(1, uuid.toString());
        return statement;
    }

    @Override
    protected void handleResult(ResultSet resultSet) throws SQLException {
        Player player = CorePlayer.getPlayer(uuid);
        if(player != null){
            CorePlayerData playerDatabaseData = null;
            //todo do rank setups etc here as per the plugin
            if(resultSet.next()){
                try {
                    CorePlayer.store(new CorePlayerData(
                            player,
                            resultSet.getInt("id"),
                            uuid,
                            resultSet.getString("last_ip"),
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
                    ));
                    String[] groupIds = resultSet.getString("all_groups").split(",");
                    for (String groupdId: groupIds){
                        //todo Groups
                    }
                    LoggerUtils.info("Core Player: " + player.getName() + " successfully loaded for with Core UUID: " + uuid);
                    (new CorePlayerLoadedEvent(player)).callEvent();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }else {
                LoggerUtils.info("Data was not found for " + player.getName() + " Initiating new registration");
                PrimesCore.getInstance().getMySQLProvider().scheduleTask(new PlayerRegisterTask(uuid, player));
            }
        }else {
            LoggerUtils.warn("Player disconnected while data was being loaded");
        }
        resultSet.close();
    }
}
