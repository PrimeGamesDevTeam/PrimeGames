package net.primegames.core.gamemode;

import lombok.Getter;
import net.primegames.core.PrimesCore;

abstract public class GameMode {

    @Getter
    private static GameMode instance;

    @Getter
    private final GameModeId id;

    public GameMode(GameModeId id) {
        this.id = id;
        instance = this;
    }

    abstract public void enable();

    abstract public void disable();

    public PrimesCore getPlugin() {
        return PrimesCore.getInstance();
    }
}
