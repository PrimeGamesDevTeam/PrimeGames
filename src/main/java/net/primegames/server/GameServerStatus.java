package net.primegames.server;

import lombok.Getter;

public enum GameServerStatus {

    ALPHA(0),
    BETA(1),
    PRODUCTION(2),
    COMING_SOON(3),
    WHITELIST(4),
    OFFLINE(5);

    @Getter
    private final int id;
    GameServerStatus(int id) {
        this.id = id;
    }

    public static GameServerStatus getById(int id) {
        for (GameServerStatus status : values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        return null;
    }

}

