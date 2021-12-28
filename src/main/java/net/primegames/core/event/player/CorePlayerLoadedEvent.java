package net.primegames.core.event.player;

import lombok.Getter;
import net.primegames.core.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CorePlayerLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final CorePlayer playerData;

    public CorePlayerLoadedEvent(Player player, CorePlayer playerData) {
        super(true);
        this.player = player;
        this.playerData = playerData;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
