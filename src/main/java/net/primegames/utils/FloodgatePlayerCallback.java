package net.primegames.utils;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public interface FloodgatePlayerCallback {

    void call(Player player, FloodgatePlayer floodgatePlayer);
}
