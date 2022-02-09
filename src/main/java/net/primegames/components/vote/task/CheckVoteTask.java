package net.primegames.components.vote.task;

import com.google.gson.Gson;
import net.primegames.components.vote.data.VoteTaskResponse;
import net.primegames.components.vote.data.VoteSite;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheckVoteTask extends VoteTask {

    private final UUID uuid;
    private final boolean sendResult;

    public CheckVoteTask(VoteSite site, Player player, boolean sendResult) {
        super(site);
        this.uuid = player.getUniqueId();
        this.sendResult = sendResult;
    }

    @Override
    protected HttpUriRequest onRun() {
        String checkUrl = site.getCheckUrlFor(uuid);
        return checkUrl != null ? new HttpGet(checkUrl) : null;
    }

    @Override
    protected void onResponse(String response, int lineNumber) {
        terminateReader();
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        Gson gson = new Gson();
        VoteTaskResponse responseObject = gson.fromJson(response, VoteTaskResponse.class);
        if (responseObject.voted) {
            if (responseObject.claimed) {
                if (sendResult){
                    player.sendMessage("§rYou have already claimed your vote on " + site.getVote() + ". Thank you for your support!");
                }
            } else {
                site.claimVote(player);
            }
        } else {
            player.sendMessage("§cYou have not voted on " + site.getVote() + ".");
        }
    }
}
