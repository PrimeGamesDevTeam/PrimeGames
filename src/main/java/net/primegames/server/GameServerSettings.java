package net.primegames.server;

import lombok.Data;


@Data
public class GameServerSettings {

    private final GameMode gameMode;
    private final String address;
    private final String icon;
    /**
     * Current game server identifier
     */
    private String identifier;
    private GameServerStatus status;

    public GameServerSettings(String $identifier, GameMode gameMode, GameServerStatus status, String address, String icon){
        this.identifier = $identifier;
        this.gameMode = gameMode;
        this.status = status;
        this.address = address;
        this.icon = icon;
    }

    GameServerSettings(String $identifier, GameMode gameMode, GameServerStatus status){
        this.identifier = $identifier;
        this.gameMode = gameMode;
        this.status = status;
        this.address = "";
        this.icon = "";
    }

    public GameServerSettings(String $identifier, GameMode gameMode, String address){
        this.identifier = $identifier;
        this.gameMode = gameMode;
        this.address = address;
        status = GameServerStatus.PRODUCTION;
        this.icon = "";
    }

    public GameServerSettings(String $identifier, GameMode gameMode){
        this.identifier = $identifier;
        this.gameMode = gameMode;
        status = GameServerStatus.PRODUCTION;
        this.address = "";
        this.icon = "";
    }

}
