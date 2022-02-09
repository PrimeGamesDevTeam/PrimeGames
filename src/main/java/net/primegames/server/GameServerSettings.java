package net.primegames.server;

import lombok.Data;


@Data
public class GameServerSettings {

    private final GameMode gameMode;
    private final String address;
    private final int port;
    private final String icon;
    /**
     * Current game server identifier
     */
    private String identifier;
    private GameServerStatus status;

    public GameServerSettings(String $identifier, GameMode gameMode, GameServerStatus status, String address, int port, String icon) {
        this.identifier = $identifier;
        this.gameMode = gameMode;
        this.status = status;
        this.address = address;
        this.port = port;
        this.icon = icon;
    }

}
