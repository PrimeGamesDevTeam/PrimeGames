package net.primegames.core.gamemode;

import lombok.Getter;

abstract public class GameMode {

    @Getter
    private final GameModeId id;

    public GameMode(GameModeId id) {
        this.id = id;
    }

    abstract public void enable();

    abstract public void disable();

}
