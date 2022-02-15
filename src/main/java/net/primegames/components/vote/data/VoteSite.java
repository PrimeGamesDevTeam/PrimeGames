package net.primegames.components.vote.data;

import lombok.Getter;
import lombok.NonNull;
import net.primegames.PrimeGames;
import net.primegames.components.vote.VoteComponent;
import net.primegames.components.vote.task.CheckVoteTask;
import net.primegames.components.vote.task.SendClaimedVoteTask;
import net.primegames.player.BedrockPlayer;
import net.primegames.player.BedrockPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class VoteSite{

    @Getter
    private final String name;
    @Getter
    private final String vote;
    private final String check;
    private final String claim;

    private VoteSite(String name, String vote, String check, String claim) {
        this.name = name;
        this.vote = vote;
        this.check = check;
        this.claim = claim;
    }

    public String getCheckUrlFor(@NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return getCheckUrlFor(player);
    }

    public String getCheckUrlFor(@NonNull Player player) {
        return check.replace("{USERNAME}", correctUserName(player));
    }

    public String getClaimUrlFor(@NonNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return getClaimUrlFor(player);
    }

    public String getClaimUrlFor(@NonNull Player player) {
        return claim.replace("{USERNAME}", correctUserName(player));
    }

    public void claimVote(@NonNull Player player) {
        PrimeGames.getInstance().getPlugin().getServerSettings().onVoteClaim(player, this);
        CompletableFuture.runAsync(new SendClaimedVoteTask(this, player));
        VoteParty.getInstance().onVote();
    }

    public void checkVoteFor(@NonNull Player player, boolean sendResult) {
        CompletableFuture.runAsync(new CheckVoteTask(this, player, sendResult));
    }

    public String correctUserName(Player player) {
        BedrockPlayer bedrockPlayer = BedrockPlayerManager.getInstance().getPlayer(player);
        if (bedrockPlayer != null) {
            return bedrockPlayer.getUsername();
        }
        return player.getName();
    }
}
