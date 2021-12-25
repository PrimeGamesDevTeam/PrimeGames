package net.primegames.core.gamemode;

import lombok.Getter;
import lombok.NonNull;
import net.primegames.factions.PrimesFactions;
import net.primegames.prison.PrimesPrison;
import net.primegames.skyblock.PrimesSkyBlock;
import net.primegames.survival.PrimesSurvival;


public enum GameModeId {

    SURVIVAL("Survival", PrimesSurvival.class),
    PRISON("Prison", PrimesPrison.class),
    SKYBLOCK("Skyblock", PrimesSkyBlock.class),
    FACTIONS("Factions", PrimesFactions.class);

    @Getter
    private final String identifier;
    @Getter
    private final Class<? extends GameMode> gameModeClass;

    GameModeId(String identifier, Class<? extends GameMode> gameModeClass){
        this.identifier = identifier;
        this.gameModeClass = gameModeClass;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    @NonNull
    public static GameModeId getGameModeId(String identifier){
        for(GameModeId gameModeId : values()){
            if(gameModeId.getIdentifier().equalsIgnoreCase(identifier)){
                return gameModeId;
            }
        }
        throw new IllegalArgumentException("No GameMode with the identifier " + identifier + " exists!");
    }

}
