package net.primegames.server;

import lombok.Data;

@Data
public class GameServer {

    private final String identifier;
    private final int port;
    private final int playerAmount;
    private final int capacity;
    private final String software;
    private final String imageUrl;
    private final GameServerStatus status;
    private GameMode gameMode;
    private String address;


    public GameServer(String identifier, GameMode gameMode, String address, int port, int playerAmount, int capacity, String software, String imageUrl, GameServerStatus status) {
        this.identifier = identifier;
        this.gameMode = gameMode;
        this.address = address;
        this.port = port;
        this.playerAmount = playerAmount;
        this.capacity = capacity;
        this.software = software;
        this.imageUrl = imageUrl;
        this.status = status;
    }
}
