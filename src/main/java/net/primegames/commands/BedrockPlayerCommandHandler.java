package net.primegames.commands;

import net.primegames.economy.shop.forms.ShopForm;
import net.primegames.player.CorePlayerManager;
import net.primegames.utils.BedrockPlayerCallback;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.HashMap;

public class BedrockPlayerCommandHandler implements Listener {

    private static HashMap<String, BedrockPlayerCallback> commands = new HashMap<>();

    public static void handle(String command, BedrockPlayerCallback callback) {
        commands.put(command, callback);
    }

    public BedrockPlayerCommandHandler(){
        handle("/shop", new BedrockPlayerCallback() {
            @Override
            public void call(Player player) {
                ShopForm.init(player);
                ignore(DeviceOs.UWP);
            }
        });
    }


    public static void setCommands(HashMap<String, BedrockPlayerCallback> commands) {
        BedrockPlayerCommandHandler.commands = commands;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        FloodgatePlayer fPlayer = CorePlayerManager.getInstance().getFloodGatePlayer(player);
        if (fPlayer != null && !fPlayer.getDeviceOs().equals(DeviceOs.UWP)) {
            Command command = Bukkit.getCommandMap().getCommand(event.getMessage().split(" ")[0].replace("/", ""));
            if (command != null) {
                if (command.getPermission() == null || player.hasPermission(command.getPermission())){
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
