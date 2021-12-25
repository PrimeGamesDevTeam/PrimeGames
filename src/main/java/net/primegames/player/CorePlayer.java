package net.primegames.player;

import net.primegames.PrimesCore;
import net.primegames.groups.GroupIds;
import net.primegames.providor.task.player.PlayerLoadTask;
import org.bukkit.entity.Player;
import org.geysermc.api.Geyser;
import org.geysermc.api.session.Connection;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CorePlayer {

    private static final Map<UUID, CorePlayerData> playerData = new HashMap<>();
    private static final Map<UUID, Player> players = new HashMap<>();

    public static void store(CorePlayerData player) {
        playerData.put(player.getPlayer().getUniqueId(), player);
    }

    public static CorePlayerData getCoreData(Player player) {
        UUID uuid = getUuid(player);
        if (uuid != null){
            return playerData.get(uuid);
        }
        return null;
    }

    public static void remove(UUID uuid) {
        playerData.remove(uuid);
        players.remove(uuid);
    }

    public static Map<UUID, Player> getPlayers() {
        return players;
    }

    public static Player getPlayer(UUID uuid) {
        Player player = players.get(uuid);
        if (player != null && player.isOnline()) {
            return player;
        }
        return null;
    }

    public static void load(UUID uuid, Player player) {
        players.put(uuid, player);
        PrimesCore.getInstance().getMySQLProvider().scheduleTask(new PlayerLoadTask(uuid, player));
    }

    //gets original xbox Uuid
    public static UUID getUuid(Player player){
        FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        if(fPlayer != null){
            Connection connection = Geyser.api().connectionByXuid(fPlayer.getXuid());
            if(connection != null) {
                return connection.uuid();
            }
        }
        return null;
    }
}
