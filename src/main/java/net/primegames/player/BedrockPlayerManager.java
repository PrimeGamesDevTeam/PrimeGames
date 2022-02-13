package net.primegames.player;

import lombok.Getter;
import lombok.Setter;
import net.primegames.PrimeGames;
import net.primegames.providor.task.player.PlayerLoadTask;
import net.primegames.utils.BedrockPlayerCallback;
import net.primegames.utils.LoggerUtils;
import org.bukkit.entity.Player;
import org.geysermc.api.Geyser;
import org.geysermc.api.session.Connection;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BedrockPlayerManager {

    @Getter
    private static BedrockPlayerManager instance;

    /**
     * Key: UUID provided by spigot
     * Example: players.put(Player.getUniqueId(), CorePlayer)
     */
    private final Map<UUID, BedrockPlayer> players = new HashMap<>();

    /**
     * Key: UUID provided by spigot aka serverUUID
     * Value: Bedrock UUID aka bedrockUUID
     */
    @Getter
    private final Map<UUID, UUID> playerUUIDs = new HashMap<>();

    public BedrockPlayerManager() {
        instance = this;
    }

    public void addPlayer(BedrockPlayer player) {
        players.put(player.getPlayer().getUniqueId(), player);
    }

    public BedrockPlayer getPlayer(Player player) {
        if (!isFloodGatePlayer(player)) {
            return null;
        }
        return players.get(player.getUniqueId());
    }

    public void remove(Player player) {
        players.remove(player.getUniqueId());
        players.remove(player.getUniqueId());
    }

    public boolean isFloodGatePlayer(Player player) {
        return getFloodGatePlayer(player) != null;
    }

    public void initPlayer(Player player) {
        FloodgatePlayer floodgatePlayer = getFloodGatePlayer(player);
        if (floodgatePlayer != null) {
            Connection connection = Geyser.api().connectionByXuid(floodgatePlayer.getXuid());
            if (connection != null) {
                playerUUIDs.put(player.getUniqueId(), connection.uuid());
                PrimeGames.getInstance().getMySQLprovider().scheduleTask(new PlayerLoadTask(connection.uuid(), player, connection.name()));
                //todo check if bedrock player has a linked java account and if yes then save it to database: (javaUuid, bedrockUuid, xboxUuid)
                //todo and if player is not liked then try to delete store from database
            } else {
                LoggerUtils.error("Could not get Geyser Connection for floodgate/bedrock player: " + player.getName());
            }
        } else {
            LoggerUtils.info(player.getName() + "is not a floodgate/bedrock player");
            //todo check if java player is a linked player, if yes then extract bedrockUuid and xboxUuid from Database
        }
    }

    //Check if the player is a linked player in floodgate
    private void initLinkedPlayer(Player player, BedrockPlayerCallback callback) {
        FloodgateApi api = FloodgateApi.getInstance();
        api.getPlayerLink().isLinkedPlayer(player.getUniqueId()).thenAcceptAsync(isLinked -> {
            if (isLinked) {
                //callback.call(player);
            } else {
                //todo
            }
        });
    }

    public FloodgatePlayer getFloodGatePlayer(Player player) {
        return FloodgateApi.getInstance().getPlayer(player.getUniqueId());
    }

}
