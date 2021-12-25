package net.primegames.core.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CorePlayerLoadedEvent extends Event {

    private final HandlerList handlerList;
    private final Player player;

    public CorePlayerLoadedEvent(Player player) {
        super(true);
        this.player = player;
        handlerList = new HandlerList();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }
}
