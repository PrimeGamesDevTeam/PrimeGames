package net.primegames.core.player;

import lombok.Getter;
import net.primegames.core.PrimesCore;
import net.primegames.core.providor.task.player.PlayerLoadTask;
import net.primegames.core.utils.Callback;
import net.primegames.core.utils.LoggerUtils;
import org.bukkit.entity.Player;
import org.geysermc.api.Geyser;
import org.geysermc.api.session.Connection;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CorePlayerManager {

    @Getter
    private static CorePlayerManager instance;

    /**
     * Key: UUID provided by spigot
     * Example: players.put(Player.getUniqueId(), CorePlayer)
     */
    private final Map<UUID, CorePlayer> players = new HashMap<>();

    public CorePlayerManager() {
        instance = this;
    }

    public void addPlayer(CorePlayer player) {
        players.put(player.getPlayer().getUniqueId(), player);
    }

    public CorePlayer getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public void remove(Player player) {
        players.remove(player.getUniqueId());
    }


    public void initPlayer(Player player){
        FloodgatePlayer floodgatePlayer = getFloodGatePlayer(player);
        if (floodgatePlayer != null){
            Connection connection = Geyser.api().connectionByXuid(floodgatePlayer.getXuid());
            if (connection != null){
                PrimesCore.getInstance().getMySQLProvider().scheduleTask(new PlayerLoadTask(connection.uuid(), player));
                //todo check if bedrock player has a linked java account and if yes then save it to database: (javaUuid, bedrockUuid, xboxUuid)
                //todo and if player is not liked then try to delete store from database
            } else {
                LoggerUtils.error("Could not get Geyser Connection for floodgate/bedrock player: " + player.getName());
            }
        }else {
            LoggerUtils.info(player.getName() + "is not a floodgate/bedrock player");
            //todo check if java player is a linked player, if yes then extract bedrockUuid and xboxUuid from Database
        }
    }

    //Check if the player is a linked player in floodgate
    private void initLinkedPlayer(Player player, Callback callback){
        FloodgateApi api = FloodgateApi.getInstance();
        api.getPlayerLink().isLinkedPlayer(player.getUniqueId()).thenAcceptAsync(isLinked -> {
            if (isLinked){
                callback.call(player);
            } else {
                //todo
            }
        });
    }

    public FloodgatePlayer getFloodGatePlayer(Player player){
        return FloodgateApi.getInstance().getPlayer(player.getUniqueId());
    }

}
