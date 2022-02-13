package net.primegames.event.player;

import lombok.Getter;
import net.primegames.event.CoreEvent;
import net.primegames.player.BedrockPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BedrockPlayerChatEvent extends CoreEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final BedrockPlayer bedrockPlayer;
    @Getter
    private final String message;
    private boolean cancelled = false;

    public BedrockPlayerChatEvent(@NotNull Player player, @NotNull BedrockPlayer bedrockPlayer, @NotNull String message) {
        super(true);
        this.player = player;
        this.bedrockPlayer = bedrockPlayer;
        this.message = message;
    }

    public @NotNull static HandlerList getHandlerList() {
        return handlers;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
