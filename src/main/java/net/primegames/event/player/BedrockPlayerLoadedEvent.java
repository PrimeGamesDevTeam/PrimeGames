package net.primegames.event.player;

import lombok.Getter;
import net.primegames.player.BedrockPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BedrockPlayerLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final BedrockPlayer playerData;

    public BedrockPlayerLoadedEvent(Player player, BedrockPlayer playerData) {
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
