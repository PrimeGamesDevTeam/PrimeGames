package net.primegames.event.player;

import net.primegames.event.CoreEvent;
import org.bukkit.entity.Player;

public abstract class CorePlayerEvent extends CoreEvent {

    private Player player;

    public CorePlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
