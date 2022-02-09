package net.primegames.components.vote.task;

import com.google.gson.Gson;
import net.primegames.components.vote.VoteComponent;
import net.primegames.components.vote.data.VoteTaskResponse;
import net.primegames.components.vote.data.VoteSite;
import net.primegames.components.vote.event.VoteClaimSuccessEvent;
import net.primegames.utils.LoggerUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SendClaimedVoteTask extends VoteTask {

    private final String username;
    private final Player player;
    private final UUID uuid;

    public SendClaimedVoteTask(VoteSite site, Player player) {
        super(site);
        this.player = player;
        this.uuid = player.getUniqueId();
        this.username = site.correctUserName(player);
    }

    @Override
    protected HttpUriRequest onRun() {
        return new HttpPost(site.getClaimUrlFor(player));
    }

    @Override
    protected void onResponse(String response, int lineNumber) {
        Gson gson = new Gson();
        VoteTaskResponse checkResponse = gson.fromJson(response, VoteTaskResponse.class);
        if (checkResponse.claimed) {
            new VoteClaimSuccessEvent(uuid, site).callEvent();
            Bukkit.broadcastMessage(VoteComponent.getInstance().getPrefix() + "&b" + username + "§r has claimed a vote on §c" + site.getVote());
        } else {
            LoggerUtils.info("Vote claim failed for " + username + " on " + site.getVote());
        }
        terminateReader();
    }
}
