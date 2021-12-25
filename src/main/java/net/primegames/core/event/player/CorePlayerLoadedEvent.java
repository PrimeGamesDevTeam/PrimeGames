package net.primegames.core.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CorePlayerLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public CorePlayerLoadedEvent(Player player) {
        super(true);
        this.player = player;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }
}
