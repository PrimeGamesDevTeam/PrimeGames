package net.primegames.core.event.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CoreGroupsLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final List<Integer> groups;

    @Getter
    private final Player player;

    public CoreGroupsLoadedEvent(Player player, List<Integer> groups) {
        super(true);
        this.groups = groups;
        this.player = player;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
