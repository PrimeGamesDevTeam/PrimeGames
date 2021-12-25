package net.primegames.core.event.player;

import net.primegames.core.event.CoreEvent;
import org.bukkit.entity.Player;

public abstract class CorePlayerEvent extends CoreEvent {

    private final Player player;

    public CorePlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
