package net.primegames.components.vote.event;

import lombok.Getter;
import net.primegames.components.vote.data.VoteSite;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * called when a vote is successfully claimed on server list <@link VoteSite>>
 */
public class VoteClaimedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final String player;
    @Getter
    private final VoteSite voteSite;

    public VoteClaimedEvent(String player, VoteSite site) {
        super(true);
        this.player = player;
        this.voteSite = site;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
