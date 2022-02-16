package net.primegames.components.store.command;

import net.primegames.components.store.StoreComponent;
import net.primegames.player.BedrockPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoreCommand extends Command {

    public StoreCommand() {
        super("purchase", "store related command", "/purchase", List.of(new String[]{"buy", "store"}));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage("You must be player to use this command");
            return false;
        }
        if (!BedrockPlayerManager.getInstance().isFloodGatePlayer(player)){
            player.sendMessage(ChatColor.RED + "We currently do not have store for java players");
            player.sendMessage(ChatColor.RED + "Player contact owner on discord/mail support@primegames.net to make a purchase");
            return false;
        }
        StoreComponent.getInstance().sendMainForm(player);
        return true;
    }

}
