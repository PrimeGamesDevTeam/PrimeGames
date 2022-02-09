package net.primegames.components.vote.event;

import lombok.Getter;
import net.primegames.components.vote.data.VoteSite;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * called when a vote is successfully claimed on server list <@link VoteSite>>
 */
public class VoteClaimSuccessEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final UUID player;
    @Getter
    private final VoteSite voteSite;

    public VoteClaimSuccessEvent(UUID player, VoteSite site) {
        super(true);
        this.player = player;
        this.voteSite = site;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
