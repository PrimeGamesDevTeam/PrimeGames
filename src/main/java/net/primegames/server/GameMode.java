package net.primegames.server;

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

