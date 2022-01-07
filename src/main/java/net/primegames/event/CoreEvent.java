package net.primegames.event;

import org.bukkit.event.Event;

public abstract class CoreEvent extends Event {
    public CoreEvent(boolean async) {
        super(async);
    }

    public CoreEvent() {
    }
}
