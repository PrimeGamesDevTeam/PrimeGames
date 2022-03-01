package net.primegames.listener;

import net.primegames.utils.BedrockPlayerCallback;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;

public class BedrockPlayerCommandListener implements Listener {

    private static HashMap<String, BedrockPlayerCallback> commands = new HashMap<>();

    public BedrockPlayerCommandListener() {
    }

    public static void handle(String command, BedrockPlayerCallback callback) {
        commands.put(command, callback);
    }

    public static void setCommands(HashMap<String, BedrockPlayerCallback> commands) {
        BedrockPlayerCommandListener.commands = commands;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        FloodgatePlayer fPlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        if (fPlayer != null) {
            Command command = Bukkit.getCommandMap().getCommand(event.getMessage().split(" ")[0].replace("/", ""));
            if (command != null) {
                if (command.getPermission() == null || player.hasPermission(command.getPermission())) {
                    String commandStr = event.getMessage().split(" ")[0];
                    if (commands.containsKey(commandStr) && !commands.get(commandStr).shouldIgnore(fPlayer.getDeviceOs())) {
                        commands.get(commandStr).call(event.getPlayer());
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}