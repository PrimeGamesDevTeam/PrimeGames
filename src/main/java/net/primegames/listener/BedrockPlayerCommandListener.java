package net.primegames.listener;

import net.primegames.bedrockforms.ShopForm;
import net.primegames.bedrockforms.WarpsForm;
import net.primegames.utils.BedrockPlayerCallback;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.HashMap;

public class BedrockPlayerCommandListener implements Listener {

    private static HashMap<String, BedrockPlayerCallback> commands = new HashMap<>();

    public BedrockPlayerCommandListener() {
        handle("shop", new BedrockPlayerCallback(DeviceOs.UWP) {
            @Override
            public void call(Player player) {
                ShopForm.init(player);
            }
        });
        handle("warp", new BedrockPlayerCallback() {
            @Override
            public void call(Player player) {
                WarpsForm.init(player);
            }
        });
    }

    public static void handle(String command, BedrockPlayerCallback callback, String ...commandAliases) {
        commands.put(command.toLowerCase(), callback);
        for (String commandAlias : commandAliases) {
            commands.put(commandAlias.toLowerCase(), callback);
        }
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
                    String commandStr = command.getName();
                    if (commands.containsKey(commandStr.toLowerCase()) && !commands.get(commandStr).shouldIgnore(fPlayer.getDeviceOs())) {
                        commands.get(commandStr).call(event.getPlayer());
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
