package net.primegames.components.vote;

import com.google.gson.Gson;
import com.vexsoftware.votifier.NuVotifierBukkit;
import lombok.Getter;
import net.primegames.components.Component;
import net.primegames.components.vote.commands.VoteCommand;
import net.primegames.components.vote.data.VoteSite;
import net.primegames.components.vote.listener.VoteListener;
import net.primegames.components.vote.task.CheckAllVoteTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public final class VoteComponent implements Component {

    @Getter
    private static VoteComponent instance;
    @Getter
    private final JavaPlugin plugin;
    @Getter
    private final HashMap<String, VoteSite> voteSites = new HashMap<>();
    @Getter
    private NuVotifierBukkit votifier;

    public VoteComponent(JavaPlugin plugin) throws IOException {
        instance = this;
        this.plugin = plugin;
        Plugin nuVotifier = plugin.getServer().getPluginManager().getPlugin("NuVotifier");
        if (nuVotifier != null) {
            votifier = (NuVotifierBukkit) nuVotifier;
        } else {
            LoggerUtils.warn("NuVotifier not found, vote commands will not work!");
            return;
        }
        Bukkit.getPluginManager().registerEvents(new VoteListener(), plugin);
        Bukkit.getCommandMap().register("primevote", new VoteCommand());
        loadVoteSites();
    }

    public void checkVotes(Player player, boolean sendResponse) {
        if (voteSites.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no vote sites registered!");
        } else {
            CompletableFuture.runAsync(new CheckAllVoteTask(voteSites, player, sendResponse));
        }
    }

    private void loadVoteSites() throws IOException {
        File file = new File(plugin.getDataFolder(), "votes");
        if (!file.exists()) {
            if (file.mkdir()) {
                LoggerUtils.warn("Created vote directory, Download vote vrc files and drop in the vote directory!");
            }
        } else {
            File[] files = file.listFiles();
            ArrayList<File> voteFiles = new ArrayList<>();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.getName().endsWith(".vrc")) {
                        voteFiles.add(f);
                    } else {
                        LoggerUtils.info("Found invalid file in vote directory: " + f.getName());
                    }
                }
                if (voteFiles.size() == 0){
                    LoggerUtils.warn("No valid vote files found in vote directory, Download vote vrc files and drop in the vote directory!");
                } else {
                    for (File f : voteFiles) {
                        loadVoteFromFile(f);
                    }
                }
            } else {
                LoggerUtils.warn("No vote sites found, creating default vote site, Download vote vrc files and drop in the vote directory!");
            }
        }
    }

    private void loadVoteFromFile(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        VoteSite site = gson.fromJson(new FileReader(file), VoteSite.class);
        voteSites.put(site.getName(), site);
        LoggerUtils.success("Loaded vote site: " + site.getName());
    }

    @Override
    public @NotNull String getIdentifier() {
        return "vote.component";
    }

    public String getPrefix() {
        return "§7[§bVote§7]§r ";
    }
}
