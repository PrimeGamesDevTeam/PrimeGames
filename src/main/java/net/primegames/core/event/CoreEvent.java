package net.primegames.core.event;

import org.bukkit.event.Event;

public abstract class CoreEvent extends Event {
    public CoreEvent(boolean async) {
        super(async);
    }

    public CoreEvent() {
    }
}
