package net.primegames.components.vote.data;

import org.bukkit.entity.Player;

public interface VoteReward {

    boolean onVoteClaimed(Player event);
}
