package net.primegames.components.vote;

import com.google.gson.Gson;
import lombok.Getter;
import net.primegames.components.Component;
import net.primegames.components.vote.commands.PrimeVoteCommand;
import net.primegames.components.vote.data.VoteReward;
import net.primegames.components.vote.data.VoteSite;
import net.primegames.components.vote.listener.VoteListener;
import net.primegames.components.vote.task.CheckAllVoteTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public final class VoteComponent implements Component {

    @Getter
    private static VoteComponent instance;
    @Getter
    private final JavaPlugin plugin;
    @Getter
    private final HashMap<String, VoteSite> voteSites = new HashMap<>();
    @Getter
    private final VoteReward reward;

    public VoteComponent(JavaPlugin plugin, VoteReward reward) throws IOException {
        instance = this;
        this.plugin = plugin;
        this.reward = reward;
        Bukkit.getPluginManager().registerEvents(new VoteListener(), plugin);
        Bukkit.getCommandMap().register("primevote", new PrimeVoteCommand());
        loadVoteSites();
    }

    public void registerVoteSite(VoteSite ...voteSite) {
        for (VoteSite site : voteSite) {
            voteSites.put(site.getName(), site);
        }
    }

    public void registerVoteSite(List<VoteSite> voteSite) {
        for (VoteSite site : voteSite) {
            voteSites.put(site.getName(), site);
        }
    }

    public void attemptClaimAllVotes(Player player) {
        AtomicInteger claimed = new AtomicInteger();
        voteSites.forEach((key, value) -> {
            if (value.claimVote(player)){
                claimed.getAndIncrement();
            }
        });
        if (claimed.get() < 1) {
            player.sendMessage(ChatColor.RED + "You have no unclaimed votes! Type " + ChatColor.YELLOW + "/vote check " + ChatColor.RED + " after voting.");
        }
    }

    public void checkVotes(Player player) {
        if (voteSites.isEmpty()){
            player.sendMessage(ChatColor.RED + "There are no vote sites registered!");
        } else {
            CompletableFuture.runAsync(new CheckAllVoteTask(voteSites, player));
        }
    }

    public void listVotesSites(Player player) {
        StringBuilder builder = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        i.set(1);
        voteSites.forEach((key, value) -> builder.append(ChatColor.RESET).append(i.getAndIncrement()).append(": ").append(ChatColor.YELLOW).append(value.getVoteLink()).append(" \n"));
        player.sendMessage(builder.toString());
    }

    public boolean hasUnclaimedVotes(Player player) {
        for (VoteSite site : voteSites.values()) {
            if (site.canClaim(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    private void loadVoteSites() throws IOException {
        File file = new File(plugin.getDataFolder(), "votes");
        if (!file.exists()) {
            if(file.mkdir()){
                LoggerUtils.info("Created vote directory");
                this.createDefaultConfig();
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.getName().endsWith(".json")) {
                        if (f.getName().equals("default.json")) {
                            LoggerUtils.info("Data file found, skipping...");
                            continue;
                        }
                        Gson gson = new Gson();
                        VoteSite site = gson.fromJson(new FileReader(f), VoteSite.class);
                        voteSites.put(site.getName(), site);
                        LoggerUtils.success("Loaded vote site: " + site.getName());
                    } else {
                        LoggerUtils.info("Found invalid file in vote directory: " + f.getName());
                    }
                }
            } else {
                this.createDefaultConfig();
                LoggerUtils.error("No vote sites found, creating default vote site");
            }
        }
    }

    private void createDefaultConfig() throws IOException {
        String path = plugin.getDataFolder() + File.separator + "votes" + File.separator + "default.json";
        File file = new File(path);
        if (!file.exists()) {
            if (file.createNewFile()) {
                LoggerUtils.info("Created default vote site");
            } else {
                LoggerUtils.error("Failed to create default vote site");
            }
        }
        VoteSite site = VoteSite.Builder.create().
                name("VoteSite").
                voteLink("https://www.example.com/vote").
                claimUrl("https://www.example.com/claim").
                checkTopUrl("https://www.example.com/check").
                checkUrl("https://www.example.com/check").
                apiKey("apiKey").
                build();
        Gson gson = new Gson();
        String json = gson.toJson(site);
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();
        LoggerUtils.info("Created default vote site");
    }

    @Override
    public @NotNull String getIdentifier() {
        return "vote.component";
    }
}
