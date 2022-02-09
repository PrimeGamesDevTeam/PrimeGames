package net.primegames.components.vote.commands;

import net.primegames.components.vote.VoteComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VoteCommand extends Command {

    public VoteCommand() {
        super("vote");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                VoteComponent.getInstance().checkVotes(player, true);
            }
            return true;
        }
        sender.sendMessage("§cYou must be a player to use this command.");
        return false;
    }

    @Override
    public @NotNull String getUsage() {
        return "§aUsage: §e/vote §f<§eclaim§f|§echeck§f|§elist§f>";
    }
}
