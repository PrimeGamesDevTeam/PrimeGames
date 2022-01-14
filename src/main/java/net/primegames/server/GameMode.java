package net.primegames.server;

import lombok.Getter;

public enum GameMode {

    LOBBY(),
    SKYBLOCK(),
    SURVIVAL(),
    FACTIONS(),
    PRISON();

    public static String getGameModeList() {
        return "Lobby, Skyblock, Survival, Factions, Prison";
    }
}

