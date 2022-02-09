package net.primegames.components.vote.event;

import lombok.Getter;
import net.primegames.components.vote.data.VoteSite;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VoteClaimEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final VoteSite site;
    private boolean canceled = false;

    public VoteClaimEvent(Player player, VoteSite site) {
        this.player = player;
        this.site = site;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
