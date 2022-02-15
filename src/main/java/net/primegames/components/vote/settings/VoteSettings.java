package net.primegames.components.vote.settings;

import net.primegames.components.vote.data.VoteSite;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface VoteSettings {

    /**
     * Things to do when a player votes for the server
     * like give a vote reward
     */
    void onVoteClaim(Player event, VoteSite site);

    /**
     * when to do when there is a vote party
     */
    void onVoteParty();

    Location votePartHoloLocation();

    /**
     * number of votes required to start a vote party
     */
    int votePartyCount();
}
