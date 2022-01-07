package net.primegames.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CorePlayerRegisteredEvent extends CorePlayerEvent {

    private final HandlerList handlerList;

    public CorePlayerRegisteredEvent(Player player) {
        super(player);
        handlerList = new HandlerList();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

}
